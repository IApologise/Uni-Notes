#ifndef COMMON_H
#define COMMON_H

// Only one upper-layer process (ping client/server) is supported.
#define MAX_CONNECTIONS 1

// Max number of entries in the ARP table, because MIP Address is 8 bits,
// so to cover all possible MIP Addresses we need 2^8 = 256 entries.
#define MAX_MIP_ADDRESSES 256

// An Ethernet payload is at most 1500 bytes and the MIP header takes 4 of them (32 bits), so the largest SDU we can
// ever send is 1496 bytes. That is a multiple of 4 (SDUs must be 32-bit aligned). 1496 / 4 = 374 words, which fits in
// the 9-bit SDU length field which has a max of 2^9 - 1 = 511 words (-1 because count starts from 0).
#define MIP_HDR_LEN   4       // MIP header size.
#define MAX_SDU_BYTES 1496    // Max payload size.
#define ETH_P_MIP     0x88B5  // Link layer MIP frame identification code.
#define MIP_BROADCAST 0xFF    // MIP broadcast address.

// Upper-layer message format on the UNIX socket (both directions).
// Upper -> Lower, first byte is the destination MIP Address.
// Lower -> Upper, first byte is the source MIP Address.
#define UPPER_MSG_MAX (1 + MAX_SDU_BYTES)  // Source/Destination MIP Address (1 byte) + Message (1496 bytes).

// SDU types (3 bits available).
#define SDU_TYPE_ARP  0x01  // ARP SDU type.
#define SDU_TYPE_PING 0x02  // PING SDU type.

// Ping timeout, and ping & pong prefixes that the client and server send to each other.
#define PING_TIMEOUT_MS 1000     // How long ping waits for a pong before a timeout occurs.
#define PING_PREFIX     "PING:"  // Sending prefix.
#define PONG_PREFIX     "PONG:"  // Replying prefix.
#define PREFIX_LEN      5        // Both prefixes have same length which is 5 letters.

#endif // COMMON_H
