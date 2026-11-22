// This is a MIP Daemon which connect the upper-layer (application) with the ethernet link layer.
// It does MIP-ARP, decides TTL, and assembles/disassembles an ethernet layer datagrams.
// Usage: mipd [-h] [-d] <socket_upper> <MIP address>

#define _GNU_SOURCE  // I didn't know this existed before, and AI tells me it is redundant, but I keep it.
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>

#include <ifaddrs.h>
#include <net/if.h>
#include <net/if_arp.h>
#include <arpa/inet.h>
#include <sys/epoll.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <linux/if_packet.h>

#include "common.h"

// Some specs
#define MAC_LEN       6   // Size of a MAC Address in bytes.
#define MAC_STR_LEN   18  // Number of characters in the MAC Address (including \0).
#define ETHERNET_LEN  14  // Destination MAC address + source MAC address + ethernet type.
#define TIME_TO_LIVE  15  // TTL for unicast PDUs (no forwarding yet).
#define SOCKETS       3   // Listening socket, upper process socket, and raw socket.

// Other constants
#define MAX_FRAME       2048  // Size of a receive buffer for one Ethernet frame, because 14 + 1500 = 1514 which
                              // requires 11 bits = 2^11 = 2048 which becomes the max frame size.
#define MAX_INTERFACES  16    // Max number of Ethernet interfaces we track (arbitrarily chosen value).
// I should have maybe removed MAX_INTERFACES and made the list scale dynamically, but oh well...

// Ethernet header (packed)
struct ethernet_header {
    uint8_t destination[MAC_LEN];  // Destination MAC Address.
    uint8_t source[MAC_LEN];       // Source MAC Address.
    uint16_t type;                 // Ethernet type.
} __attribute__((packed));         /* Funny way CPU's work, need to request no padding to be added.
                                      I actually saw a video on YouTube channel "Core Dumped" about it once. */

// MIP header (unpacked)
struct mip_header {
    uint8_t destination;   // MIP Addresses (destination), 8 bits.
    uint8_t source;        // MIP Address (source), 8 bits.
    uint8_t time_to_live;  // TTL takes 4 bits, and last I checked that fits in 8 bits, so...
    uint16_t sdu_words;    // SDU length in 32-bit words (9 bits).
    uint8_t sdu_type;      // SDU type (3 bits).
};

// MIP-ARP entry indexed by MIP address
struct arp_entry {
    int valid;             // Entry starts as 0 initialized, need to differentiate between fake entries and real ones.
    uint8_t mac[MAC_LEN];  // Destination MAC Address.
    int interface_index;   // Interface on our device that has the MAC Address above.
};

// Describing interfaces found on our NIC
struct interface {
    int index;             // Interface's index (in Kernel).
    uint8_t mac[MAC_LEN];  // Our own MAC Address, i.e. source.
    char name[IFNAMSIZ];   // Same as index, but name instead of number (for debugging).
};

// Given parameters
static int debug = 0;           // Debug mode.
static uint8_t thisMipAddress;  // MIP address we are given.
static const char* socketPath;  // UNIX socked name (or ID? Whatever...).

// File descriptors
static int listeningFD = -1;     // FD to a UNIX socket that listens for incoming connection requests.
static int upperProcessFD = -1;  // FD to a Connected process.
static int rawFD = -1;           // FD to the raw socket.
static int epollFD = -1;         // Epoll FD.

// Technical stuff
static struct interface interfaces[MAX_INTERFACES];   // Ethernet interfaces we get so see.
static int interfaceCount = 0;                        // Count of valid interfaces in the interfaces[] list.
static struct arp_entry arpTable[MAX_MIP_ADDRESSES];  // MIP-ARP table, max 256 entries.

// Single upper process handling in case of an MIP-ARP miss in our table.
static int pending_used = 0;                // Whether a message is to be sent.
static uint8_t pending_dst;                 // Destination MIP Address to which we should send.
static size_t pending_len;                  // The message's length (again, that we need to send).
static uint8_t pending_sdu[MAX_SDU_BYTES];  // The message that we should send.
// We essentially save MIP header data until we get the MAC Address after we do a MIP-ARP broadcast.

// Formats MAC Address into a string (used for debugging).
static const char* mac_str(const uint8_t* mac, char* out) {
    snprintf(out, MAC_STR_LEN, "%02x:%02x:%02x:%02x:%02x:%02x",
             mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
    return out;
}

// Find interface in the interfaces[] table based on the local index given by kernel.
static struct interface* find_interface(int index) {
    for (int i = 0; i < interfaceCount; i++)
        if (interfaces[i].index == index)
            return &interfaces[i];
    return NULL;
}

// Prints out what is being done, as well as the addresses of payload, and
// also prints out the MIP-ARP table at the moment (all this is for debug mode).
static void log_event(const char* info_type,
    const uint8_t* eth_dst, const uint8_t* eth_src,
    uint8_t mip_dst, uint8_t mip_src) {

    // Setup
    char dst[MAC_STR_LEN], src[MAC_STR_LEN];  // MAC addresses to print
    if (!debug)  // Not in debug mode, do nothing.
        return;

    // Printing what is done and payload addresses.
    printf("[%s]\n  ETH-dst=%s ETH-src=%s\n  MIP-dst=%d MIP-src=%d\n",
           info_type, mac_str(eth_dst, dst), mac_str(eth_src, src), mip_dst, mip_src);

    // Printing MIP-ARP table
    printf("  MIP-ARP table:");
    int any = 0;  // Special check for a message for when there are no entries.
    for (int i = 0; i < MAX_MIP_ADDRESSES; i++) {
        if (!arpTable[i].valid)  // ARP entry not valid, skip.
            continue;
        struct interface* interface = find_interface(arpTable[i].interface_index);  // Getting interface.
        printf("\n    MIP %3d -> %s via %s", i, mac_str(arpTable[i].mac, dst),
            interface ? interface->name : "?");  // In case there is no name to be printed.
        any = 1;  // Valid ARP entry found.
    } printf(any ? "\n" : " (empty)\n");  // No valid ARP entries -> printing that it is empty.
}

// Encoding a MIP header into one 32-bit word (in network byte order, i.e. using htonl).
// MIP header format: dst=8b, src=8b, ttl=4b, sdu_words=9b, sdu_type=3b.
static uint32_t mip_pack(uint8_t dst, uint8_t src, uint8_t ttl, uint16_t sdu_words, uint8_t sdu_type) {
    uint32_t header = (uint32_t) dst << 24 | (uint32_t) src << 16 |  // Moving destination & source to their spots.
                      (uint32_t) (ttl & 0xF) << 12 |  // Moving TTL to its spot (only first 4 bits initialized = 0xF).
                      (uint32_t) (sdu_words & 0x1FF) << 3 |  // SDU words is 9 bits = 0x1FF.
                          (sdu_type & 0x7);  // SDU type is 3 bits = 0x7, bitshift not needed, is last anyways.
    return htonl(header);  // Formatting the order & return
}

// Decoding a MIP header, works similarly to how mip_pack does.
static void mip_unpack(const uint8_t* packet, struct mip_header* header) {
    uint32_t word;
    memcpy(&word, packet, sizeof(word));  // Getting data stored at "packet".
    word = ntohl(word);  // Formatting order.

    // Decoding the packet information (self explanatory, look at mip_pack).
    header->destination = word >> 24 & 0xFF;
    header->source = word >> 16 & 0xFF;
    header->time_to_live = word >> 12 & 0xF;
    header->sdu_words = word >> 3 & 0x1FF;
    header->sdu_type = word & 0x7;
}

// Fills interfaces and returns the count, returns -1 if failed to get address
static int discover_interfaces(void) {
    struct ifaddrs* list = NULL;
    if (getifaddrs(&list) < 0) {  // Getting address, have to free later.
        perror("Error: Failed to get Address.");  // Failed to get address.
        return -1;
    }

    // Getting the available interfaces (until our list is full or none are left to get).
    for (struct ifaddrs* pointer = list; pointer && interfaceCount < MAX_INTERFACES; pointer = pointer->ifa_next) {

        // Out of interfaces to get, or the interface is not relevant?
        if (!pointer->ifa_addr || pointer->ifa_addr->sa_family != AF_PACKET) {
            continue;  // Then fetch the next one.
        }  // Can we send the data over this interface?
        if (pointer->ifa_flags & IFF_LOOPBACK || !(pointer->ifa_flags & IFF_UP)) {  // Is interface to us or not usable?
            continue;  // If loopback or not usable, then fetch the next one.
        }
        struct sockaddr_ll* ll = (struct sockaddr_ll*) pointer->ifa_addr;  // Interpreting as AF_PACKET.
        if (ll->sll_hatype != ARPHRD_ETHER) {  // Checking whether it is a MAC Address.
            continue;
        } if (ll->sll_halen != MAC_LEN) {  // Checking if MAC Address of proper length.
            continue;
        }
        // By this point the interface is validated. Saving it to our list.
        interfaces[interfaceCount].index = ll->sll_ifindex;  // Saving interface index.
        memcpy(interfaces[interfaceCount].mac, ll->sll_addr, MAC_LEN);  // Saving our MAC Address to it.
        strncpy(interfaces[interfaceCount].name, pointer->ifa_name, IFNAMSIZ - 1);  // Saving interface name.
        interfaceCount++;  // Interface added, increment count.
    }
    freeifaddrs(list);  // Freeing memory allocated by getifaddrs() method.
    return interfaceCount;
}

// Builds ethernet frame & sends it via raw socket. Return 0 on success, -1 on failure.
static int send_frame(int interface_index, const uint8_t* destination_mac, uint8_t destination_mip,
                      uint8_t time_to_live, uint8_t type, const uint8_t* sdu, size_t sdu_length) {
    // Setup.
    uint8_t frame[ETHERNET_LEN + MIP_HDR_LEN + MAX_SDU_BYTES];  // Initializing frame.
    struct interface* interface = find_interface(interface_index);  // Finding interface.
    if (!interface || sdu_length % 4 != 0 || sdu_length > MAX_SDU_BYTES) {
        return -1;  // Interface not found, that or wrong SDU length.
    }

    // Assembling ethernet header.
    struct ethernet_header* eth = (struct ethernet_header*) frame;
    memcpy(eth->destination, destination_mac, MAC_LEN);  // Setting destination MAC Address.
    memcpy(eth->source, interface->mac, MAC_LEN);        // Setting source MAC Address.
    eth->type = htons(ETH_P_MIP);                  // Setting ethernet type.

    // Assembling MIP header.
    uint32_t header = mip_pack(destination_mip, thisMipAddress, time_to_live, sdu_length / 4, type);

    // Adding to frame.
    memcpy(frame + ETHERNET_LEN, &header, MIP_HDR_LEN);         // Adding MIP header past Ethernet to frame.
    memcpy(frame + ETHERNET_LEN + MIP_HDR_LEN, sdu, sdu_length);  // Adding SDU past MIP header to frame.

    // Interface via which to send the frame data through.
    struct sockaddr_ll to = {0};
    to.sll_family = AF_PACKET;
    to.sll_protocol = htons(ETH_P_MIP);
    to.sll_ifindex = interface_index;
    to.sll_halen = MAC_LEN;
    memcpy(to.sll_addr, destination_mac, MAC_LEN);

    // Sending frame via given interface (through the raw file descriptor).
    if (sendto(rawFD, frame, ETHERNET_LEN + MIP_HDR_LEN + sdu_length, 0,
               (struct sockaddr*) &to, sizeof(to)) < 0) {
        perror("Error: Failed to send the frame.");  // Failed to send.
        return -1;
    }
    // Printing what we did (if in debug mode).
    log_event(type == SDU_TYPE_ARP ? "SEND ARP" : "SEND PING/PONG",  // Log message depends on whether ARP or not.
              destination_mac, interface->mac, destination_mip, thisMipAddress);
    return 0;
}

// Store a MIP Address -> MAC Address map in our ARP table (kinda basic).
static void arp_learn(uint8_t mip, const uint8_t* mac, int interface_index) {
    arpTable[mip].valid = 1;                          // Setting it as valid (of course)
    memcpy(arpTable[mip].mac, mac, MAC_LEN);        // Setting the name
    arpTable[mip].interface_index = interface_index;  // Setting the index
}

// Broadcasting the MIP-ARP request on every interface.
static void send_arp_request(uint8_t target) {
    static const uint8_t broadcast[MAC_LEN] = {0xff,0xff,0xff,0xff,0xff,0xff};  // Broadcast MAC Address.
    uint32_t sdu = htonl((uint32_t) target << 23);  // Preparing to send request
    for (int i = 0; i < interfaceCount; i++) {
        send_frame(interfaces[i].index, broadcast, MIP_BROADCAST, 1, SDU_TYPE_ARP,
           (uint8_t *)&sdu, sizeof(sdu));  // Sending identical request via each interface
    }
}

// A response to the MIP-ARP (if us, we could spoof...).
static void send_arp_response(uint8_t to_mip, const uint8_t* to_mac, int interface_index) {
    uint32_t sdu = htonl(1u << 31 | (uint32_t) thisMipAddress << 23);  // Preparing to send our MIP Address
    send_frame(interface_index, to_mac, to_mip, 1, SDU_TYPE_ARP,
               (uint8_t*) &sdu, sizeof(sdu));  // Sending response
}

// Accept/Reject connection request from an upper process.
static void accept_upper(void) {
    int fd = accept(listeningFD, NULL, NULL);  // Create a new unix_sock, get a new FD for it.
    if (fd < 0) {
        return;  // Connection request rejected.
    }
    if (upperProcessFD != -1) {
        close(fd);  // A second application tried to connect while one is already connected, reject.
        return;
    }
    // New connection with the upper process established.
    upperProcessFD = fd;
    struct epoll_event ev = {.events = EPOLLIN, .data.fd = fd};  // Creating read event.
    epoll_ctl(epollFD, EPOLL_CTL_ADD, fd, &ev);  // Adding it to epoll.
    if (debug) {
        printf("Upper process connected.\n");  // Printing action (debug mode).
    }
}

// Close connection to upper process (for when upper process disconnected or an error occured).
static void close_upper(void) {
    epoll_ctl(epollFD, EPOLL_CTL_DEL, upperProcessFD, NULL);  // Removing from epoll.
    close(upperProcessFD);  // Closing file descriptor.
    upperProcessFD = -1;  // Setting to none.
    pending_used = 0;  // Connection could be closed during ARP-MIP request, no need for that anymore.
    if (debug) {
        printf("Upper process disconnected.\n");
    }
}

// Handling information we get from upper process.
static void handle_upper(void) {
    uint8_t buffer[UPPER_MSG_MAX];  // Preparing to receive message.
    ssize_t n = recv(upperProcessFD, buffer, sizeof(buffer), 0);  // We woke up, Reading message.
    if (n <= 0) {
        close_upper();  // Woke up but no message -> upper process terminated connection, we close ours too.
        return;
    }
    // Processing received message
    uint8_t dst = buffer[0];  // Destination MIP Address
    size_t sdu_len = (size_t) n - 1;  // Message length (first byte is the destination).
    if (sdu_len == 0 || sdu_len % 4 != 0 || dst == MIP_BROADCAST || dst == thisMipAddress)  // Validating...
        return;  // Message poorly formatted, reject incoming message

    // Checking if we got the MIP in our ARP table.
    if (arpTable[dst].valid) {  // Checking if we know where to forward to.
        send_frame(arpTable[dst].interface_index, arpTable[dst].mac, dst,  // We do, formatting & forwarding.
                   TIME_TO_LIVE, SDU_TYPE_PING, buffer + 1, sdu_len);
        return;  // Upper handling done.
    }
    // In case of a miss in our MIP-ARP table, we save data and send a MIP-ARP broadcast.
    pending_used = 1;
    pending_dst = dst;
    pending_len = sdu_len;
    memcpy(pending_sdu, buffer + 1, sdu_len);
    send_arp_request(dst);  // Sending ARP request.
}

// Handling information we get from our raw connection.
static void handle_raw(void) {
    uint8_t buffer[MAX_FRAME];  // Preparing to receive information.
    struct sockaddr_ll from;  // Who we got it from.
    socklen_t flen = sizeof(from);

    // We woke up, reading message
    ssize_t n = recvfrom(rawFD, buffer, sizeof(buffer), 0, (struct sockaddr*) &from, &flen);
    if (n < ETHERNET_LEN + MIP_HDR_LEN || from.sll_pkttype == PACKET_OUTGOING)  //
        return;

    // Unpacking information...
    struct ethernet_header* ethernet = (struct ethernet_header*) buffer;  // Setup first
    struct mip_header header;
    mip_unpack(buffer + ETHERNET_LEN, &header);  // Unpacking MIP header.
    size_t sdu_len = (size_t) header.sdu_words * 4;  // Words -> Bytes.
    const uint8_t* sdu = buffer + ETHERNET_LEN + MIP_HDR_LEN;  // Skipping header info, jumping to SDU.

    // Some validation checks.
    if (sdu_len > MAX_SDU_BYTES || ETHERNET_LEN + MIP_HDR_LEN + sdu_len > (size_t) n) {
        return;  // Incorrect size was given.
    } if (header.destination != thisMipAddress && header.destination != MIP_BROADCAST)
        return;  // Not directed to us, drop the packet.

    // Log what has happened & what was done (if in debug mode).
    log_event(header.sdu_type == SDU_TYPE_ARP ? "RECV ARP" : "RECV PING/PONG",
              ethernet->destination, ethernet->source,
              header.destination, header.source);

    // Message we got is a MIP-ARP resolution broadcast/response?
    if (header.sdu_type == SDU_TYPE_ARP && sdu_len >= 4) {
        uint32_t word;
        memcpy(&word, sdu, sizeof(word));
        word = ntohl(word);
        int is_response = word >> 31 & 1;     // Gonna check if it is a response to our broadcast.
        uint8_t address = word >> 23 & 0xFF;  // Gonna check if we are the ones who the message is directed to.

        // Not a response and directed to us? Then we echo.
        if (!is_response && address == thisMipAddress) {  // We could spoof that :D
            arp_learn(header.source, ethernet->source, from.sll_ifindex);  // Might as well add this to our table.
            send_arp_response(header.source, ethernet->source, from.sll_ifindex);  // Sending a response.
        } else if (is_response) {  // Is a response? Save info.
            arp_learn(header.source, ethernet->source, from.sll_ifindex);  // Saving info to our table.
            if (pending_used && pending_dst == header.source) {  // Did we do MIP-ARP because we didn't know it and
                pending_used = 0;                                // had to broadcast? Probably yes, resume sending.
                send_frame(from.sll_ifindex, ethernet->source, pending_dst,
                    TIME_TO_LIVE,SDU_TYPE_PING, pending_sdu, pending_len);  // Sending postponed data.
            }
        }  // Message we got is a PING from other side? If so, do a PONG then.
    } else if (header.sdu_type == SDU_TYPE_PING && upperProcessFD >= 0) {  // Connection to upper process must still be open.
        uint8_t message[UPPER_MSG_MAX];  // Preparing to forward the received packet information (PING) to the upper process.
        message[0] = header.source;
        memcpy(message + 1, sdu, sdu_len);
        send(upperProcessFD, message, 1 + sdu_len, MSG_NOSIGNAL);  // Forwarding the message.
    }
}

// Creates a UNIX listening & raw sockets, creates events for wake up calls: Uses epoll.
static int setup_sockets(void) {
    struct sockaddr_un addr = {.sun_family = AF_UNIX};  // Local IPC, i.e. between this PC's processes
    strncpy(addr.sun_path, socketPath, sizeof(addr.sun_path) - 1);  // Setting connection ID (name)

    // Creating a struct unix_sock to listen for incoming connections (kernel does this on our behalf)
    listeningFD = socket(AF_UNIX, SOCK_SEQPACKET, 0);
    rawFD = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_MIP));  // Ethernet frame FD
    if (listeningFD < 0 || rawFD < 0) {  // Validating...
        perror("Error: Need root privileges.");  // Probably?
        return -1;
    }
    // Referencing unix_sock by creating an inode and adding it to inode <-> socket table (kernel does this)
    unlink(socketPath);  // Unlinking previously created sockets first (if they existed)
    if (bind(listeningFD, (struct sockaddr*) &addr, sizeof(addr)) < 0 ||
        listen(listeningFD, MAX_CONNECTIONS) < 0) {  // Setting the unix_sock's listening flag on (kernel does this)
        perror("Error: Binding the socket or listening for incoming connections failed.");
        return -1;
    }
    // Going for event based FD system
    struct epoll_event listeningEvent = {.events = EPOLLIN, .data.fd = listeningFD};  // Upper connection requests
    epoll_ctl(epollFD, EPOLL_CTL_ADD, listeningFD, &listeningEvent);
    struct epoll_event rawEvent = {.events = EPOLLIN, .data.fd = rawFD};  // Incoming ethernet packets
    epoll_ctl(epollFD, EPOLL_CTL_ADD, rawFD, &rawEvent);
    return 0;  // Setup complete
}

// Setting up sockets using methods above & handling events.
// Exit with "Ctrl + C". Returns 0 if all ok, else 1 on usage or setup errors.
int main(int argc, char** argv) {

    // Searching for flags (if any).
    int opt;
    while ((opt = getopt(argc, argv, "dh")) != -1) {
        switch (opt) {
        case 'd':  // Enter debug mode flag.
            debug = 1;
            break;
        case 'h':  // Help with program usage flag.
            printf("Usage: %s [-h] [-d] <socket_upper> <MIP address>\n", argv[0]);
            return 0;
        default:  // Undefined: Perhaps the user doesn't know how to use the program?
            fprintf(stderr, "Usage: %s [-h] [-d] <socket_upper> <MIP address>\n", argv[0]);
            return 1;
        }
    }  // Now using optind which is getopt's global variable pointing to the next argv element (i.e. an index).
    // In this case it now points to the next mandatory input, i.e. a non-flag argument.
    if (argc - optind != 2) {
        fprintf(stderr, "Error: socket path and MIP address are required.\n"
                        "Usage: %s [-h] [-d] <socket_upper> <MIP address>\n", argv[0]);
        return 1;
    }
    // Reference pointers to the 2 given mandatory parameters (optional but clean).
    socketPath = argv[optind];
    char *end;  // Characters that could not be converted from string to float.
    long address = strtol(argv[optind + 1], &end, 10);  // Converting string to long (base 10).
    if (end == argv[optind + 1] || *end != '\0' || address < 0 || address > MAX_MIP_ADDRESSES - 2) {  // Validating...
        fprintf(stderr, "Error: MIP address must be a number from 0 to %d.\n", MAX_MIP_ADDRESSES - 2);
        return 1;
    }
    // Making it so that the output is flushed immediately when a new line is printed.
    setvbuf(stdout, NULL, _IOLBF, 0);  // This is equivalent to doing fflush(stdout) after every "\n".

    // Getting our own MIP Address.
    thisMipAddress = (uint8_t) address;

    // Creating epoll & getting its file descriptor.
    epollFD = epoll_create1(0);
    if (epollFD < 0 || discover_interfaces() <= 0 || setup_sockets() < 0) {
        fprintf(stderr, "Error: setup failed, no interfaces/permission.\n");
        return 1;
    } printf("MIP Daemon:\n  MIP address = %d\n  Interface Count = %d\n  Socket = %s\n",
        thisMipAddress, interfaceCount, socketPath);  // Printing MIP Daemon info.

    // List of epoll events to look out for.
    struct epoll_event events[SOCKETS];
    for (;;) {  // Main loop
        int count = epoll_wait(epollFD, events, SOCKETS, -1);  // Waiting for a wake up call.
        if (count < 0) {  // What happened?
            if (errno == EINTR) {  // Kernel just woke epoll up early because program got killed?
                continue;
            } perror("Error: Something happened with epoll.");  // Nope, some other error...
            return 1;
        }
        // Got a valid wake up call, checking the events.
        for (int i = 0; i < count; i++) {
            int fd = events[i].data.fd;  // Which file descriptor.
            if (fd == listeningFD)
                accept_upper();  // Upper process, accept if not busy (can only have 1).
            else if (fd == rawFD)
                handle_raw();  // Incoming message from outside.
            else if (fd == upperProcessFD)
                handle_upper();  // Incoming message from upper process, forwarding...
        }
    }
}
