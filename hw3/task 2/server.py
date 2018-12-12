from socket import *

host = '192.168.42.138'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)
udp_socket.bind(addr)


currentPacketNum=0
d=0
e=0
buffer=[]
def messageParse(message):
	global currentPacketNum
	if message[:2] == "d=":
		global d
		d=int(message[2:])
		currentPacketNum=0

		global buffer
		buffer=[]
	else:
		splittedMessage=message.split('XX',1)
		currentPacketNum+=1
		sequencNumber=splittedMessage[0]
		if int(sequencNumber) != int(currentPacketNum):
			buffer.append(None)
		splittedMessage2=splittedMessage[1].rsplit("E=",1)
		plaintText=splittedMessage2[0]
		if len(splittedMessage2) >1:
			global e
			e=splittedMessage2[1]
		buffer.append(plaintText)
	
		print "received packet with sequence # ",sequencNumber
	
		return plaintText

while True:
	message, addr = udp_socket.recvfrom(10)
	messageParse(message)
	answer="received d="+str(d)
	udp_socket.sendto(answer,addr)
	for _ in range(d):
		message, addr = udp_socket.recvfrom(100)
		messageParse(message)

	for pair in buffer:
		#print pair
		udp_socket.sendto(pair,addr)
    
udp_socket.close()