CC = gcc
CFlags = -g -Wall

all:
	$(CC) $(CFlags) mipd.c -o mipd
	$(CC) $(CFlags) ping_client.c -o ping_client
	$(CC) $(CFlags) ping_server.c -o ping_server

clean:
	rm -f mipd ping_client ping_server