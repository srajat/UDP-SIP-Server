# VoIP Communication Using SIP

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The <strong>VoIP COMMUNICATION USING SIP</strong> is a server based software that creates and manages a session, where a session is considered an exchange of data between an association of participants. It provides user friendly environment for users to communicate between themselves.
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;It uses <b>SIP (Session Initiation Protocol)</b> which is one of the most common protocol used in VoIP technology. VoIP is a technology that allows us to deliver voice and multimedia content over the internet. It is one of the cheapest way to communicate anytime anywhere with internet availability. 
	
	
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This is a <b>JAVA based server program</b> which handles clients. The software makes excessive use of <b>JAVA Socket API</b> to receive requests and forward respective responses. It maintains detail of registered clients i.e IP address and local port and their numbers. Upon recieveing a call request by any registered user, it generates an appropriate SIP response and forwards it its destination. This software can also act as a proxy server that can communicate as a proxy with another instance of same software running on different machine. 
	
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>This readme contains screenshots of working of the software and basic guidelines of its usage. This is an introduction as well as a step by step method of how to use this software.</b>

## Table of contents

- [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Using NetBeans IDE](#using-netbeans-ide)
    - [Technologies Used](#technologies-used)
- [Features](#features)
    - [Registration of Users](#register)
    - [Normal Flow](#normal)
    - [Cancellation Process](#cancel)
    - [When Busy](#busy)
- [Installation and Usage Guide](#guide)
- [Links](#links)

## Installation <a name='installation'></a>

### Prerequisites <a name='prerequisites'></a>

1. NetBeans 8.0.2 IDE
2. JDK 1.6 or higher
3. Phoner/Jitsi (Virtual phones) or any IP enabled Phone
4. Windows (for client if using Phoner)
5. A server

### Using NetBeans 8.0.2 IDE <a name='using-netbeans-ide'></a>
<pre>
1. Create a new Project in NetBeans 
2. Fork this repo  
3. Copy all the files in this repo to project's folder  
4. Run the project (Specific details later in this readme)  
</pre>

### Technologies Used <a name='technologies-used'></a>

1. <b>JAVA Socket API</b>
2. SIP Protocol (RFC3261)
3. UDP (User Datagram Protocol)

## Features <a name='features'></a>

### Registration Of Users <a name='register'></a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A user sends a REGISTER request to the server. The request includes user’s contact list. The SIP Server validates the user’s credentials and if it succeeds then user is successfully registered and server replies with 200 OK response.   

### Normal Flow <a name='normal'></a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In this flow of control, the caller makes a call through an INVITE request which is handled by the server. The server figures out the correct recipient of the call and forwards it. It also sends back a TRYING 100 response back to the caller. The receiver recognizes the INVITE message and there is a ringing bell on the phone of the receiver. He picks up the call and a successful RTP connection is established between both the users. Any user can drop the call by sending a BYE request to the other user. The other user sends an appropriate ACK response and the session terminates.

### Cancellation Process <a name='cancel'></a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;When the user who is calling, ends the call then a CANCEL request is sent to the server. The server forwards the CANCEL request to the receiver. Receiver sends an 200 OK and a 487 REQUEST TERMINATED response. The session is then cancelled successfully.  

### When Busy <a name='busy'></a>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;When the receiver terminates the call during the ringing phase, then the caller receives 486 BUSY HERE response. In this scenario, reciever is busy and sends a 486 BUSY HERE response to caller’s INVITE. Note that the non 2xx response is acknowledged on a hop-by-hop basis instead of end-to-end. Also note that many SIP UAs will not return a 486 response, as they have multiple lines and other features.

## Installation And Usage Guide <a name='guide'></a>

<b>1. DOWNLOAD PHONER ON CLIENT</b> </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/phoner.png) </br>
<b>2. SET UP NETBEANS 8.0.2 or later with JDK 1.6 or later</b> </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/netbeans.png) </br>
<b>3. RUN THE CODE ON A SERVER SPECIFYING THE PORT AND IP</b> </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/1.png) </br>
clients will register one by one... </br>
![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/2.png) </br>
<b>4. RUN PHONER ON CLIENT COMPUTER AND SET SERVER IP AS REGISTRAR</b> </br></br>
<b>5. NOW DIAL THE NUMBER OF ANY REGISTERED USER AND CALL</b> </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/3.png) </br>
<b>6. RTP SESSION IS ETABLISHED WHEN ‘CONNECTED’ IS DISPLAYED</b> </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/4.png) </br>
<b>7. CLICK THE RED ‘END CALL’ WHEN FINISHED TALKING</b> </br>
<b>8. LOG SCREEN DURING ‘BUSY HERE’ </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/5.png) </br>
9. LOG SCREEN DURING NORMAL FLOW </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/6.png) </br>
10. LOG SCREEN DURING CANCELATION </br></br>
 ![alt tag](https://raw.githubusercontent.com/srajat/UDP-SIP-Server/master/images/7.png) </br>
</b>
## Links <a name='links'></a>

Visit me:     [Rajat Saxena](http://www.rajatsaxena.in/)

Contact me:     <rajat8171@gmail.com>
