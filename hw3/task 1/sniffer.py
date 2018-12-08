
import socket
from struct import *
import datetime
import pcapy
import sys

serverIP=""
clientIP=""
 
def main(argv):
    #get server and 
    global serverIP
    global clientIP
    serverIP=argv[1]
    clientIP=argv[2]
    print "server IP: ",argv[1],", client IP: ", argv[2]
    devices = pcapy.findalldevs()
    print "Available devices:"
    for d in devices :
        print d
    print "Choose device to sniff:"
    dev = raw_input("Enter device name to sniff : ")

    '''
    # Arguments here are:
    #   device
    #   snaplen (maximum number of bytes to capture _per_packet_)
    #   promiscious mode (1 for true)
    #   timeout (in milliseconds)
    '''
    cap = pcapy.open_live(dev , 65536 , 1 , 0)
 
    #endles loop for sniffing
    while(1) :
        (header, packet) = cap.next()
        parse_packet(packet)
 

#function to parse a packet
def parse_packet(packet) :
     
    #get ethernet header do detect IP protocol
    eth_length = 14
    eth_header = packet[:eth_length]
    eth = unpack('!6s6sH' , eth_header) #it's generic way to unpack ethernet header
    eth_protocol = socket.ntohs(eth[2]) #convert from network to host byte order
    
    #8 is IP protocol, we need it only
    if eth_protocol == 8 :
        # Parse IP header, ip header minimal size is 20 bytes therefore will take it do detect protocols
        # without ethernet header
        ip_header = packet[eth_length:20+eth_length]
         
        #now unpack them :)
        iph = unpack('!BBHHHBBH4s4s' , ip_header) #it's generic way to unpack IP header
 
        #calculate ip header
        version_ihl = iph[0]
        ihl = version_ihl & 0xF
        iph_length = ihl * 4
 
        protocol = iph[6]
        s_addr = socket.inet_ntoa(iph[8]) #get source ip and convert from ip to string
        d_addr = socket.inet_ntoa(iph[9]) #get destination ip and convert from ip to string

        #detect only UDP packets
        if protocol == 17 :
            #detect packets between server and client only, without any trash
            if((str(s_addr)==str(clientIP) and str(d_addr)==str(serverIP)) or (str(s_addr)==str(serverIP) and str(d_addr)==str(clientIP))):
                u = iph_length + eth_length
                udph_length = 8
                udp_header = packet[u:u+udph_length]

                #unpack udp header
                udph = unpack('!HHHH' , udp_header) 
                
                source_port = udph[0]
                dest_port = udph[1]
                print "catched packet between: "+str(s_addr)+":"+str(source_port)+" --> "+str(d_addr)+":"+str(dest_port)
                
                #calculate all headers sizes for extracting data from packet
                header_size = eth_length + iph_length + udph_length
                
                #get data from the packet
                data = packet[header_size:]
                print data
                print #new line to separate packets

 
main(sys.argv)