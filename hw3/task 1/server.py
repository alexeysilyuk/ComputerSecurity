from socket import *

host = '192.168.42.138'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)
udp_socket.bind(addr)

def messageParse(message):
	splittedMessage=message.split('|',1)
	sequencNumber=splittedMessage[0]
	plaintText=splittedMessage[1]
	print "packet #"+sequencNumber+" received"
	return plaintText

while True:
	message, addr = udp_socket.recvfrom(120)

	plaint_text = messageParse(message)
	udp_socket.sendto(plaint_text,addr)

    
udp_socket.close()