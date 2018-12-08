
from socket import *

host = '192.168.42.138'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)

udp_socket.bind(addr)

def messageParse(message):
	splittedMessage=message.split('XX',1)
	sequencNumber=splittedMessage[0]
	plaintText=splittedMessage[1]
	print "Sequence ID: ",sequencNumber
	print "Data	  : ",plaintText
	return plaintText

while True:

	message, addr = udp_socket.recvfrom(100)

	plaint_text = messageParse(message)
	udp_socket.sendto(plaint_text,addr)

    
udp_socket.close()