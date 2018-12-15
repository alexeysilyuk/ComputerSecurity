from socket import *

import sys

from random import randrange
import time

# create socket with remote server
host = '192.168.42.140'
port = 12321
addr = (host,port)
udp_socket = socket(AF_INET, SOCK_DGRAM)

packetNumber=0

# create list of words for message
message = "He was so proud of his little girl.  It was her very first day of school.  He walked with her to school that day,  And she held his hand all the way.  They walked together quiet and sad,  A little girl and her loving dad.  Into the school her father led,  But he almost cried when she said,  Daddy, Daddy, please dont go.  Dont leave me here all alone.  I will miss you if you go away,  And I might need you; cant you stay?  Little Daughter, please don't cry.  You'll be okay, so dry your eyes.  You have our memories in your heart.  We're together though we're apart. He sat up front on her wedding day  And cried as his daughter walked away.  Later that night he watched her dance.  He sat there waiting for his chance.  The band started to play their song.  Father and daughter danced along.  She looked at him and saw a tear  Then leaned and whispered in his ear,  Daddy, Daddy, I have to go.  I hate to leave you all alone.  I'll miss you when I go away,  But if you need me then I'll stay. Little Daughter, I'll be just fine.  I'll love you always; you are mine.  I have our memories in my heart.  We're together though we're apart.  She came in his room and kissed his head  Then sat next to his hospital bed.  He took her hand and held it tight  And wished he had the strength to fight.  They sat together, quiet and sad,  A daughter and her dying dad.  He saw the tears she tried to hide.  She looked at him and then she cried.  Daddy, Daddy, please don't go.  Don't leave me here all alone.  I'll miss you when you go away.  I still need you; you have to stay. Little Daughter, I love you so.  I want to stay but have to go.  I'll always be here in your heart.  We're together though we're apart".split(" ")


# makes xor between 2 strings, cc*100 adding zeroes in case of short string
xor = lambda ss,cc: ''.join(chr(ord(s)^ord(c)) for s,c in zip(ss,cc*100))


# generate message for sending
def generateMessage():

	# randomly get set of words from words list
	data = ""

	for _ in range(randrange(1,len(message))):
		random_index = randrange(0,len(message))
		data = data + " "+message[random_index]

	return data

# send udp datagram and not waiting to response
def sendPacket(data):
	udp_socket.sendto(data, addr)
	data = bytes.decode(data)
	
# send packet with d value
def sendDPacket(dValue):
	sendPacket("d="+str(dValue))	#send d value to server
	responce = udp_socket.recvfrom(1024)
	print responce[0]	# and wait for responce, kind of negotiation

# xoring all datagrams
def calculateEvalue(totalpackets,data):
	e=data[:100]
	data=data[100:]
	for _ in range(totalpackets-1):
		e=xor(e,data[:100])
		data=data[100:]

	return e

# split message to datagrams, sending d value, sending all packets
def sendMessage(data):
	totalpackets=(len(data)/100 )+1	# calculate total amount of packets
	d=randrange(1,totalpackets)		# generate d value
	sendDPacket(d)					# send d value to server

	e=calculateEvalue(totalpackets,data)

	# split message to datagrams, to firs d packets, append to tail "E=" with calculated e value
	for packetID in range(totalpackets):
		header=str(packetID+1)+"/"+str(totalpackets)+"|"	# header separated from message with "|" symbol
		packetData=data[:100]
		if packetID < d:
			packet=header+packetData+"||E="+e
		else:
			packet=header+packetData
		
		data=data[100:]

		# simulates packet lost, not sending 3-rd packet (indices are: 0,1,2..)
		if packetID ==2:
			print "lost Packet: "+packetData
			continue

		#send generated packet to server
		sendPacket(packet)

	return totalpackets

# receive echo from server
def receiveAnswer(packetsAmount):
	print "Starting to echo from server..."
	print "\n**************************  PACKET [ ",packetNumber," ]  **************************\n"
	for _ in range(packetsAmount):
		data = udp_socket.recvfrom(1024)
		print(data[0])

	print "\n**************************  PACKET [ ",packetNumber," ] END  ***********************\n"


# generate packet, sends it and receive answers
def generatePacket():
	global packetNumber
	packetNumber+=1
	packetsAmount=1

	# generate random message
	data = generateMessage()
	# split message to datagrams and send them to server
	packetsAmount = sendMessage(data)

	print "Packet #"+str(packetNumber)+", splitted to "+str(packetsAmount)+" datagrams was sent"

	# wait for answer from server to echo packets
	receiveAnswer(packetsAmount)



#send packet each 3 second
while True:
	generatePacket()
	time.sleep(3)

udp_socket.close()