import http.server
import ssl
import sys
import socketserver

if(len(sys.argv) < 3):
	print("Not enough arguments")
else:
	cert = sys.argv[1]
	key = sys.argv[2]
	context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
	context.load_cert_chain(cert, key)
	server_address = ("localhost", 443)
	handler = http.server.SimpleHTTPRequestHandler

	with socketserver.TCPServer(server_address, handler) as httpd:
		httpd.socket = context.wrap_socket(httpd.socket, server_side=True)
		httpd.serve_forever()
