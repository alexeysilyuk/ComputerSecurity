from socket import *

### socket config ###
host = '192.168.42.140'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)
udp_socket.bind(addr)
#####################

### global variables ###
currentPacketNum=0
d=0
e=0
packetBuffer=[]
########################

# makes xor between 2 strings
xor = lambda ss,cc: ''.join(chr(ord(s)^ord(c)) for s,c in zip(ss,cc*100))


# parse d value from packet
def receiveDvalue(message):
	global d
	#check if d packet arrived
	if message[:2] == "d=":
		print "received d="+str(message[2:])
		d=int(message[2:]) # cut d value from message
		answer="reveived d="+str(d)+" by server"
		udp_socket.sendto(answer,addr) # send message to client to know taht we got d value

		# renew packets counter
		global currentPacketNum
		currentPacketNum=0

# splite header of type 1/12, while 1 is curent packet id and 12 is total packets
def parseHeader(header):
	return header.split('/',1)

# parse received message
def parseMessage(message):
	global currentPacketNum
	currentPacketNum+=1
	
	# header separated from data with "|" sign
	parsedMessage=message.split("|",1)
	header=parsedMessage[0]

	# detect if packet contains E value, in right of datagram message
	parsedMessage=parsedMessage[1].split("||E=",1)
	if len(parsedMessage)>1:
		global e
		e=parsedMessage[1]

	# get current packet id and total of packets
	currentID,totalPackets=parseHeader(header)

	#detect packet lost
	if int(currentID) != int(currentPacketNum):
		packetBuffer.append("None") # add "None" instead of lost packet for future recovering
		currentPacketNum+=1

	# add received message to buffer
	packetBuffer.append(parsedMessage[0])

	print "packet "+str(currentID)+ "/"+ str(totalPackets)+" received"

	# detect if it's last datagram in sequence is arrived, to know when to start echo
	if int(currentID) == int(totalPackets):
		return True
	else:
		return False 

# calculate e value based on all correct received messages
def calculateEvalue():
	calculated_e=packetBuffer[0]
	for i,line in enumerate(packetBuffer):
		if i==0:				# skip first message, beacuse we took it already 
			continue

		# with "None" we signed in buffer place of lost packet
		# if current value not "None", xor it 
		if line != "None":
			calculated_e=xor(calculated_e,line)

	return calculated_e


# after receiving all messages, send received packets back
def sendResponce():
	global packetBuffer

	# calculate e value of all received datagrams
	calculated_e=calculateEvalue()

	# find index of lost packet in buffer and calculate it's value
	# it's can recover only one one lost datagram
	for index, line in enumerate(packetBuffer):
		if line == "None":
			packetBuffer[index]=str(xor(calculated_e,e))
			print "Recovered packet:\nID: "+str(index+1)+", Data: "+packetBuffer[index]
			break

	# send all packets back to client
	print "Starting to send responce"
	for line in packetBuffer:
		udp_socket.sendto(line,addr)

	del packetBuffer[:]

while True:
	#receive first packet with "d" value
	message, addr = udp_socket.recvfrom(1000)
	print "***************** Receiving packet *****************"
	receiveDvalue(message)

	# receive packets till last datagram arrived
	while True:
		message, addr = udp_socket.recvfrom(1000)
		isLastMessage=parseMessage(message)
		if isLastMessage is True:
			# after receiving all datagrams, send then back to client
			sendResponce()
			break
	print "***************** Packet received  *****************"

# close connection
udp_socket.close()