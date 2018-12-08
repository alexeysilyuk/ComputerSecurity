
import socket
from struct import *
import datetime
import pcapy
import sys

serverIP=""
clientIP=""
 
def main(argv):
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
 
#Convert a string of 6 characters of ethernet address into a dash separated hex string
def eth_addr (a) :
    b = "%.2x:%.2x:%.2x:%.2x:%.2x:%.2x" % (ord(a[0]) , ord(a[1]) , ord(a[2]), ord(a[3]), ord(a[4]) , ord(a[5]))
    return b
 
#function to parse a packet
def parse_packet(packet) :
     
    #parse ethernet header
    eth_length = 14
     
    eth_header = packet[:eth_length]
    eth = unpack('!6s6sH' , eth_header)
    eth_protocol = socket.ntohs(eth[2])
    
 
    #Parse IP packets, IP Protocol number = 8
    if eth_protocol == 8 :
        #Parse IP header
        #take first 20 characters for the ip header
        ip_header = packet[eth_length:20+eth_length]
         
        #now unpack them :)
        iph = unpack('!BBHHHBBH4s4s' , ip_header)
 
        version_ihl = iph[0]
        version = version_ihl >> 4
        ihl = version_ihl & 0xF
 
        iph_length = ihl * 4
 
        ttl = iph[5]
        protocol = iph[6]
        s_addr = socket.inet_ntoa(iph[8]);
        d_addr = socket.inet_ntoa(iph[9]);

        if(str(s_addr)==str(clientIP) and str(d_addr)==str(serverIP)):
            print 'Version : ' + str(version) + ' IP Header Length : ' + str(ihl) + ' TTL : ' + str(ttl) + ' Protocol : ' + str(protocol) + ' Source Address : ' + str(s_addr) + ' Destination Address : ' + str(d_addr)

            #detect only UDP packets
            if protocol == 17 :
                u = iph_length + eth_length
                udph_length = 8
                udp_header = packet[u:u+8]
    
                #now unpack them :)
                udph = unpack('!HHHH' , udp_header)
                
                source_port = udph[0]
                dest_port = udph[1]
                length = udph[2]
                checksum = udph[3]
                
                print 'Source Port : ' + str(source_port) + ' Dest Port : ' + str(dest_port) + ' Length : ' + str(length) + ' Checksum : ' + str(checksum)
                
                h_size = eth_length + iph_length + udph_length
                data_size = len(packet) - h_size
                
                #get data from the packet
                data = packet[h_size:]
                
                print 'Data : ' + data

        print
 
if __name__ == "__main__":
  main(sys.argv)