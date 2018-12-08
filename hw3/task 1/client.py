from socket import *

import sys

from random import randrange
import time

# create socket with remote server
host = '192.168.42.138'
port = 12321

# create list of words for message
message = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc".split(" ")

# generate message for sending
def generateMessage():

	# randomly get set of words from words list
	data = ""

	for _ in range(randrange(len(message))):
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
	data = "1XX"+data
	sendPacket(data)

def splitMessageToDatagrams(data):
	packets=(len(data)/100 )+1
	print "packets: ",packets,", length: ",len(data)
	for packetID in range(packets):
		send_now=str(packetID)+"XX"+data[:100]
		data=data[100:]
		print "sending: "+send_now
		sendPacket(send_now)



def sendMessage():

	data = generateMessage()

	if len(data) > 100:
		splitMessageToDatagrams(data)
	else:
		sendOneDatagram(data)

while True:

	sendMessage()
	time.sleep(3)