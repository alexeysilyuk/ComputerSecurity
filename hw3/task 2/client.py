from socket import *

import sys

from random import randrange
import time

# create socket with remote server
host = '192.168.42.138'
port = 12321

packetNumber=0

# create list of words for message
message = "He was so proud of his little girl.  It was her very first day of school.  He walked with her to school that day,  And she held his hand all the way.  They walked together quiet and sad,  A little girl and her loving dad.  Into the school her father led,  But he almost cried when she said,  \"Daddy, Daddy, please don't go.  Don't leave me here all alone.  I'll miss you if you go away,  And I might need you; can't you stay?\"  \"Little Daughter, please don't cry.  You'll be okay, so dry your eyes.  You have our memories in your heart.  We're together though we're apart.\"    He sat up front on her wedding day  And cried as his daughter walked away.  Later that night he watched her dance.  He sat there waiting for his chance.  The band started to play their song.  Father and daughter danced along.  She looked at him and saw a tear  Then leaned and whispered in his ear,  \"Daddy, Daddy, I have to go.  I hate to leave you all alone.  I'll miss you when I go away,  But if you need me then I'll stay.\"  \"Little Daughter, I'll be just fine.  I'll love you always; you are mine.  I have our memories in my heart.  We're together though we're apart.\"    She came in his room and kissed his head  Then sat next to his hospital bed.  He took her hand and held it tight  And wished he had the strength to fight.  They sat together, quiet and sad,  A daughter and her dying dad.  He saw the tears she tried to hide.  She looked at him and then she cried.  \"Daddy, Daddy, please don't go.  Don't leave me here all alone.  I'll miss you when you go away.  I still need you; you have to stay.\"  \"Little Daughter, I love you so.  I want to stay but have to go.  I'll always be here in your heart.  We're together though we're apart.\"".split(" ")

#makes xor between 2 strings
def xor(s1,s2):  
	if(s1 == 0):
		return s2
	if(s2 == 0):
		return s1
	return ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(s1,s2))


# generate message for sending
def generateMessage():

	# randomly get set of words from words list
	data = ""

	for i in range(randrange(len(message))):
		random_index = randrange(len(message))
		data = message[random_index]+" "+data

	return data


def sendPacket(data):
	addr = (host,port)
	udp_socket = socket(AF_INET, SOCK_DGRAM)
	udp_socket.sendto(data, addr)
	data = bytes.decode(data)
	data = udp_socket.recvfrom(1024)
	print(data[0])
	udp_socket.close()
	
def sendOneDatagram(data):
	e=0
	e=xor(e,data)
	data = "1XX"+data+"E="+e
	sendPacket(data)

def splitMessageToDatagrams(data):
	packets=(len(data)/100 )+1
	d=randrange(packets)
	dataBackup=data
	e=0
	sendOneDatagram("d="+str(d))	#send d value to server
	for _ in range(packets):
		e=xor(e,data[:100])
		data=data=data[100:]

	data=dataBackup
	for packetID in range(packets):
		if packetID<= d:
			msg=str(packetID+1)+"XX"+data[:100]+"E="+e
		else:
			msg=str(packetID+1)+"XX"+data[:100]
		
		data=data[100:]
		sendPacket(msg)




def sendMessage():
	data = generateMessage()
	global packetNumber
	packetNumber+=1
	print "\n**************************  PACKET [ ",packetNumber," ]  **************************\n"
	if len(data) > 100:
		splitMessageToDatagrams(data)
	else:
		sendOneDatagram(data)
	print "\n**************************  PACKET [ ",packetNumber," ] END  ***********************\n"

while True:

	sendMessage()
	time.sleep(3)