// This is a Ping Client, and all it does is it pings some MIP host via MIP Daemon.
// It tracks how long it took for a response (a Pong), that is if no timeout happened (1 sec).
// Usage: ping_client [-h] <socket_lower> <message> <destination_host>

#define _GNU_SOURCE  // I didn't know this existed before, and AI tells me it is redundant, but I keep it.
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <time.h>

#include <poll.h>
#include <sys/socket.h>
#include <sys/un.h>

#include "common.h"

// Current time from the clock in milliseconds with nanosecond precision.
static double now(void) {  // Used AI for this because I've never done that before.
    struct timespec time;
    clock_gettime(CLOCK_MONOTONIC, &time);
    return time.tv_sec * 1000.0 + time.tv_nsec / 1e6;
}

// Returns 0 if a matching pong arrived, and 1 on an error/timeout.
int main(int argc, char** argv) {

    // Searching for flags (if any).
    int opt;
    while ((opt = getopt(argc, argv, "h")) != -1) {
        switch (opt) {
        case 'h':  // Help with program usage flag.
            printf("Usage: %s [-h] <socket_lower> <message> <destination_host>\n", argv[0]);
            return 0;
        default:  // Undefined: Perhaps the user doesn't know how to use the program?
            fprintf(stderr, "Usage: %s [-h] <socket_lower> <message> <destination_host>\n", argv[0]);
            return 1;
        }
    }  // Now using optind which is getopt's global variable pointing to the next argv element (i.e. an index).
    // In this case it now points to the next mandatory input, i.e. a non-flag argument.
    if (argc - optind != 3) {
        fprintf(stderr, "Error: MIP daemon socket, message, and destination MIP address are required.\n"
                        "Usage: %s [-h] <socket_lower> <message> <destination_host>\n", argv[0]);
        return 1;
    }
    // Reference pointers to the 3 given mandatory parameters (optional but clean).
    const char *socket_lower = argv[optind];
    const char *message = argv[optind + 1];
    char *end;  // Characters that could not be converted from string to float.
    long destination_host = strtol(argv[optind + 2], &end, 10);  // Converting string to long (base 10).
    if (*end != '\0' || MAX_MIP_ADDRESSES - 2 < destination_host || destination_host < 0) {  // Validating...
        fprintf(stderr, "Error: Destination address must be a MIP address from 0 to %d.\n", MAX_MIP_ADDRESSES - 2);
        return 1;
    }

    // Payload is "PING:" + message + '\0', zero-padded to a multiple of 4 bytes.
    // Here we are also verifying the SDU length to be at most 374 words.
    size_t text_len = PREFIX_LEN + strlen(message) + 1;
    size_t sdu_len = (text_len + 3) / 4 * 4;  // Round up to the nearest multiple of 4.
    if (sdu_len > MAX_SDU_BYTES) {  // Checking if message too large.
        fprintf(stderr, "Error: Message too long, max %d characters.\n",
                MAX_SDU_BYTES - PREFIX_LEN - 1);
        return 1;
    }
    // Preparing message to be sent via the MIP Daemon.
    uint8_t out[UPPER_MSG_MAX] = {0};  // Initializing everything with 0 bytes (we also get \0 at the end this way).
    out[0] = (uint8_t) destination_host;  // First byte is a MIP Address.
    memcpy(out + 1, PING_PREFIX, PREFIX_LEN);  // Adding prefix.
    memcpy(out + 1 + PREFIX_LEN, message, strlen(message));  // Adding message itself.

    // Connecting to the MIP Daemon's UNIX socket.
    struct sockaddr_un addr = {.sun_family = AF_UNIX};  // Local IPC, i.e. between this PC's processes.
    strncpy(addr.sun_path, socket_lower, sizeof(addr.sun_path) - 1);  // Setting connection ID (name).

    // Creating a struct unix_sock to listen for incoming connections (kernel does this on our behalf).
    int mipdFD = socket(AF_UNIX, SOCK_SEQPACKET, 0);  // Returns an FD.
    if (mipdFD < 0) {perror("socket"); return 1;}  // Validating this FD.
    if (connect(mipdFD, (struct sockaddr*) &addr, sizeof(addr)) < 0) {  // Connecting to MIP Daemon's socket.
        perror("Error: Failed to connect to MIP Daemon.");
        return 1;
    }

    // Send package to MIP Daemon, and start measuring time.
    double start_time = now();
    if (send(mipdFD, out, 1 + sdu_len, 0) < 0) {
        perror("Error: Failed to send the message to MIP Daemon.");
        return 1;
    }

    // Waiting for the answer, only responding to a specific PONG, ignoring the rest.
    for (;;) {  // Infinite loop without compiler warnings, and it is technically faster than while(1) too.
        double time_left = PING_TIMEOUT_MS - (now() - start_time);  // Time left until timeout
        if (time_left <= 0) {  // Timeout?
            printf("timeout\n");  // Yup...
            close(mipdFD);
            return 1;
        }
        // AI suggested poll because (it is simpler, and because we only got 1 connection anyways).
        struct pollfd pfd = {.fd = mipdFD, .events = POLLIN};
        int response = poll(&pfd, 1, (int) time_left + 1);
        if (response < 0) {  // What happened?
            if (errno == EINTR) {  // Kernel just woke poll up early because program got killed?
                continue;
            } perror("Error: Failed polling a MIP Daemon.");  // Nope, some other error...
            return 1;
        } if (response == 0) {  // Nothing to read?
            continue;  // Going back to start then.
        }

        // Poll woke up with no errors, we got a response! Prepare to take it in.
        uint8_t in[UPPER_MSG_MAX + 1];  // First byte is the source address, rest is message.
        ssize_t n = recv(mipdFD, in, UPPER_MSG_MAX, 0);
        if (n == 0) {  // MIP Daemon decided to close the connection?
            fprintf(stderr, "Connection to the MIP Daemon closed.\n");
            return 1;  // We didn't even get our PONG yet, so ye, that's an error...
        } if (n < 0) {  // Uhh... What happened?
            perror("Error: Failed to receive a message from the MIP Daemon.");
            return 1;
        }
        // Reading message
        in[n] = '\0';  // Payload is text, adding a string termination at the end.
        const char *payload = (const char *) in + 1;  // Message starts after the first byte.

        // Checking if this payload is the one we were looking for.
        // We do this by comparing the message we sent before with the one we received.
        if (n > 1 + PREFIX_LEN && strncmp(payload, PONG_PREFIX, PREFIX_LEN) == 0 &&  // And some validation...
            strcmp(payload + PREFIX_LEN, message) == 0) {
            printf("RTT: %.3f ms\n", now() - start_time);
            close(mipdFD);
            return 0;
        }
    }
}
