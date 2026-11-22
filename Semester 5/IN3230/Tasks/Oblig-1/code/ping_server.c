// This is a Server Client, and all it does is it listens for a ping from some host via MIP Daemon.
// If it gets a ping, it responds with a pong (same message as what the ping sent) to the sender.
// Usage: ping_server [-h] <socket_lower>

#define _GNU_SOURCE  // I didn't know this existed before, and AI tells me it is redundant, but I keep it.
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <sys/un.h>

#include "common.h"

// Returns 0 if exited safely (e.g. if MIP Daemon closed the connection), and 1 on some error.
int main(int argc, char** argv) {

    // Searching for flags (if any).
    int opt;
    while ((opt = getopt(argc, argv, "h")) != -1) {
        switch (opt) {
        case 'h':  // Help with program usage flag.
            printf("Usage: %s [-h] <socket_lower>\n", argv[0]);
            return 0;
        default:  // Undefined: Perhaps the user doesn't know how to use the program?
            fprintf(stderr, "Usage: %s [-h] <socket_lower>\n", argv[0]);
            return 1;
        }
    }  // Now using optind which is getopt's global variable pointing to the next argv element (i.e. an index).
    // In this case it now points to the next mandatory input, i.e. a non-flag argument.
    if (argc - optind != 1) {
        fprintf(stderr, "Error: MIP Daemon socket is required.\n"
                        "Usage: %s [-h] <socket_lower>\n", argv[0]);
        return 1;
    }
    // Reference pointers to the given mandatory parameter (optional but clean).
    const char* socket_lower = argv[optind];

    // Making it so that the output is flushed immediately when a new line is printed.
    setvbuf(stdout, NULL, _IOLBF, 0);  // This is equivalent to doing fflush(stdout) after every "\n".

    // Connecting to the MIP Daemon's UNIX socket.
    struct sockaddr_un addr = {addr.sun_family = AF_UNIX};  // Local IPC, i.e. between this PC's processes.
    strncpy(addr.sun_path, socket_lower, sizeof(addr.sun_path) - 1);  // Setting connection ID (name).

    // Creating a struct unix_sock to listen for incoming connections (kernel does this on our behalf).
    int mipdFD = socket(AF_UNIX, SOCK_SEQPACKET, 0);  // Creating an FD.
    if (mipdFD < 0) {perror("socket"); return 1;}  // Validating this FD.
    if (connect(mipdFD, (struct sockaddr*) &addr, sizeof(addr)) < 0) {  // Connecting to MIP Daemon's socket.
        perror("Error: Failed to connect to MIP Daemon.");
        return 1;
    }
    printf("Connected to MIP Daemon, waiting for pings...\n");  // Success, awaiting pings.

    // Waiting for pings, responding to all.
    for (;;) {  // Infinite loop without compiler warnings, and it is technically faster than while(1) too.

        // Preparing to receive a message
        uint8_t in[UPPER_MSG_MAX + 1];  // First byte is the source address, rest is message.
        ssize_t n = recv(mipdFD, in, UPPER_MSG_MAX, 0);
        if (n == 0) {  // MIP Daemon decided to close the connection?
            printf("Connection to the MIP Daemon closed.\n");
            break;  // Sure I guess...
        } if (n < 0) {  // Uhh... What happened?
            perror("Error: Failed to receive a message from the MIP Daemon.");
            return 1;
        }
        // Reading message
        in[n] = '\0';  // Payload is text, adding a string termination at the end.
        uint8_t src = in[0];  // Getting the source to pong back to.
        const char* payload = (const char*) in + 1;  // Message starts after the first byte.

        // Validating message format & such...
        if (n <= 1 + PREFIX_LEN || strncmp(payload, PING_PREFIX, PREFIX_LEN) != 0)
            continue;  // This ain't a ping bro: Ignore it then.
        const char* message = payload + PREFIX_LEN;
        printf("Received from MIP %d: %s\n", src, message);

        // Payload is "PING:" + message + '\0', zero-padded to a multiple of 4 bytes.
        // Here we are also verifying the SDU length to be at most 374 words.
        // We reply by sending a "PONG:" + message + '\0' + zero padding to a multiple of 4 bytes.
        // And we send that to the source of the ping message, i.e. set destination = received source.
        size_t text_len = PREFIX_LEN + strlen(message) + 1;
        size_t sdu_len = (text_len + 3) / 4 * 4;  // Round up to the nearest multiple of 4.
        uint8_t out[UPPER_MSG_MAX] = {0};  // Creating message.
        out[0] = src;  // Setting first byte (destination) to the source we got message from.
        memcpy(out + 1, PONG_PREFIX, PREFIX_LEN);  // Adding "PONG:" prefix.
        memcpy(out + 1 + PREFIX_LEN, message, strlen(message));  // Echoing back the message we got.
        if (send(mipdFD, out, 1 + sdu_len, MSG_NOSIGNAL) < 0) {
            perror("Error: Failed to send the message to MIP Daemon.");  // Ok...
            return 1;
        }
    } close(mipdFD);  // Connection to MIP Daemon terminated safely, either that or program shutdown requested.
    return 0;  // So we terminate.
}
