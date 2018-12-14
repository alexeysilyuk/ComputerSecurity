from socket import *

host = '192.168.42.140'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)
udp_socket.bind(addr)

currentPacketNum=0
d=0
e=0
packetBuffer=[]

#makes xor between 2 strings
def xor(s1,s2):  
	if(s1 == 0):
		return s2
	if(s2 == 0):
		return s1
	return ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(s1,s2))

def receiveDvalue(message):
	if message[:2] == "d=":
		print "received d="+str(message[2:])
		d=int(message[2:])
		answer="reveived d="+str(d)+" by server"
		udp_socket.sendto(answer,addr)

		global currentPacketNum
		currentPacketNum=0
		return d

def parseHeader(header):
	return header.split('/',1)

def parseMessage(message):
	global currentPacketNum
	currentPacketNum+=1
	
	parsedMessage=message.split("|",1)
	header=parsedMessage[0]
	parsedMessage=parsedMessage[1].rsplit("E=",1)
	if len(parsedMessage)>1:
		global e
		e=parsedMessage[1]

	currentID,totalPackets=parseHeader(header)
	
	print "packet "+str(currentID)+ " of "+ str(totalPackets)+" received"

	sequencNumber=currentID

	#detect packet lost
	if int(sequencNumber) != int(currentPacketNum):
		packetBuffer.append("None")
		currentPacketNum+=1

		packetBuffer.append(parsedMessage[0])
	else:
		packetBuffer.append(parsedMessage[0])

	if int(currentID) == int(totalPackets):
		return True
	else:
		return False 
	
def sendResponce():
	global packetBuffer
	calculated_e=0

	# calculate e of all received messages
	for line in packetBuffer:
		if line != "None":
			calculated_e=xor(calculated_e,line)

	# find index of lost packet in buffer and calculate it's value
	# it's can recover only one one lost datagram
	for index, line in enumerate(packetBuffer):
		if line == "None":
			packetBuffer[index]=str(xor(calculated_e,e))
			print "Lost packet recovered: "+packetBuffer[index]


	print "Starting to send responce"
	for line in packetBuffer:
		print "send: "+line
		udp_socket.sendto(line,addr)
	packetBuffer=[]

while True:
	message, addr = udp_socket.recvfrom(10)
	d=receiveDvalue(message)

	while True:
		message, addr = udp_socket.recvfrom(1000)
		isLastMessage=parseMessage(message)
		if isLastMessage is True:
			sendResponce()
			break

udp_socket.close()