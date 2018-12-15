import socket
from struct import *
import datetime
import pcapy
import sys

serverIP=""
clientIP=""
 
# received argumets from terminal
# argv[1] = serverIP, argv[2] = ClientIP, argv[3] = interface name to sniff
def main(argv):
    #get server and 
    global serverIP
    global clientIP
    serverIP=argv[1]
    clientIP=argv[2]
    dev = argv[3]
    print "server IP: ",argv[1],", client IP: ", argv[2], ", interface: ",dev

    # open pcapy sniffer, dev is devicem 65536 max packet length to capture
    # 1 is promiscious mode TRUE, 0 for timeout in milliseconds
    # we need to enter promiscious mode to sniff packets on interfacem otherwise it will not work
    cap = pcapy.open_live(dev , 65536 , 1 , 0)
 
    #endles loop for sniffing
    while(1) :
        (header, packet) = cap.next()   # get next packet
        parse_packet(packet)
 

#function to parse a packet
def parse_packet(packet) :
     
    # cut ethernet header do detect IP protocol
    eth_length = 14
    eth_header = packet[:eth_length]
    eth = unpack('!6s6sH' , eth_header) # it's generic way to unpack ethernet header
    eth_protocol = socket.ntohs(eth[2]) # convert from network to host byte order
    
    #8 is IP protocol, we need it only
    if eth_protocol == 8 :
        # Parse IP header, ip header minimal size is 20 bytes therefore will take it do detect protocols
        # without ethernet header
        ip_header = packet[eth_length:20+eth_length]
         
        # now extract ip header
        iph = unpack('!BBHHHBBH4s4s' , ip_header) # it's generic way to unpack IP header
 
        # calculate ip header values
        version_ihl = iph[0]
        ihl = version_ihl & 0xF
        iph_length = ihl * 4
 
        protocol = iph[6]
        s_addr = socket.inet_ntoa(iph[8]) # get source ip and convert from ip to string
        d_addr = socket.inet_ntoa(iph[9]) # get destination ip and convert from ip to string

        # in this homework we want to detect only UDP packets, 17 is UDP protocol number
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

# run main function with received from terminal values ( serverIP, clientIP )
main(sys.argv)