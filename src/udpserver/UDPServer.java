package udpserver;

/**
 *
 * @author Rajat Saxena & Shivam Dabral & Biwas Bisht
 * @date 13/Jun/2016
 * @project UDP_Server
 * @File UDPServer.java
 */

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UDPServer
{
    private static final int ECHOMAX = 2048;    //Stores max length of recieved message in bytes
    //HashMap of REGISTERED users
    private static HashMap<String,String> REGISTERED = new HashMap<String,String>();
    //HashMap of Current Calls going on
    private static HashMap<String,callDetails> CURRENTCALLS = new HashMap<String,callDetails>();
    
    public static void main(String[] args) throws IOException
    {           
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Enter the port of the server (int): ");
        int servPort = Integer.parseInt(br.readLine());
        
        System.out.print("Enter the IP address of the server (String): ");
        String servIp = br.readLine();
        
        System.out.println("Server Started. Listening for requests....");
        
        //Create new packet to recieve into
        DatagramSocket socket = new DatagramSocket(servPort) ;
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
       
            
        for (;;)    //Loops infinitely
        {
            socket.receive(packet);     //Blocks till packet recieved
            
            InetAddress clientAddress = packet.getAddress();    //client's IP
            int clientPort = packet.getPort();      //client's Port
            
            byte[] arr = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), arr, 0, arr.length);
            String requestMsg = new String(arr);    //recieved Message
            
            
            StringTokenizer st = new StringTokenizer(requestMsg,"\r\n");
            String line1 = st.nextToken();  //stores first line of recieved message
            String typeOfMsg = line1.substring(0, line1.indexOf(" "));
            
            if("REGISTER".equals(typeOfMsg))    //If message is of type Register
            {
                registerRequest r = new registerRequest();  //new register object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow-Events".equals(feildName))
                        r.allowEvents = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Expires".equals(feildName))
                        r.expires = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //add to registered the user toRegister
                String sipUri = r.contact.substring(r.contact.indexOf(":")+1, r.contact.indexOf(";"));
                String number,ipPort;
                
                if(!sipUri.contains(">"))
                {
                    //this means there is no > (for Jitsi)
                    number = sipUri.substring(0, sipUri.indexOf("@"));
                    ipPort = sipUri.substring(sipUri.indexOf("@")+1, sipUri.length());
                }
                else
                {
                    //this means there is a > (for Phoner)
                    number = sipUri.substring(0, sipUri.indexOf("@"));
                    ipPort = sipUri.substring(sipUri.indexOf("@")+1, sipUri.length()-1);
                }
                
                //check if already registered
                boolean isRegistered = REGISTERED.containsKey(number);
                int expires = Integer.parseInt(r.expires.trim());
                
                if(!isRegistered && expires > 0)    //register
                {
                    System.out.println("Phone "+number+" is Successfully Registered at IP:PORT "+ipPort+" .");
                    REGISTERED.put(number, ipPort);
                }
                    
                else if(isRegistered && expires == 0)   //unregister
                {
                    System.out.println("Phone "+number+" is Successfully UNREGISTERED.");
                    REGISTERED.remove(number);
                }
                
                //send OK response to caller
                byte[] send = r.OK_200().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
                
            }
            
            if("INVITE".equals(typeOfMsg))  //If message is of type Invite
            {
                inviteRequest r = new inviteRequest();  //new Invite object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Type".equals(feildName))
                        r.contentType = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Early-Media".equals(feildName))
                        r.pEarlyMedia = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Preferred-Identity".equals(feildName))
                        r.prefferedIdentity = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(feildName))
                    {
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                        break;
                    }
                }
                while(st.hasMoreTokens())   //Fill data into SDP part of msg
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf("="));
                    if("v".equals(feildName))
                        r.v = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("o".equals(feildName))
                        r.o = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("s".equals(feildName))
                        r.s = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("c".equals(feildName))
                        r.c = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("t".equals(feildName))
                        r.t = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("m".equals(feildName))
                        r.m = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("a".equals(feildName))
                        r.a.add(nextLine.substring(nextLine.indexOf("=")+1,nextLine.length()));
                }
                
                //find who is calling
                String callerNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                
                //find who to send this invite
                String calleeNumber = line1.substring(line1.indexOf(":")+1,line1.indexOf("@"));
                String calleeIp = extractIpOrPort(REGISTERED.get(calleeNumber),0);
                String calleePort = extractIpOrPort(REGISTERED.get(calleeNumber),1);
                
                System.out.println("INVITE coming from "+callerNumber+" to " + calleeNumber + " .");
                
                //send trying back to caller
                byte[] send = r.TRYING_100().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
                
                //Now forward this packet to callee
                byte[] send1 = r.forwardInvite(line1, servIp, servPort).getBytes();
                DatagramPacket p1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p1.setAddress(InetAddress.getByName(calleeIp));
                p1.setPort(Integer.parseInt(calleePort.trim()));
                p1.setData(send1);
                socket.send(p1);
                
                //add details to CURRENTCALLS
                callDetails cd = new callDetails();
                cd.caller = callerNumber;
                cd.called = calleeNumber;
                
                String callId = r.callId.substring(0, r.callId.indexOf("@"));
                CURRENTCALLS.put(callId, cd);
                
            }
            
            if("SIP/2.0 180 Ringing".equals(line1))     //If message is of type Ringing
            {
                ringingRequest r = new ringingRequest();    //new Ringing Object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //extract details of who to forward
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Ringing forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward ringing
                byte[] send = r.forwardRinging(servIp).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if("SIP/2.0 200 OK".equals(line1))      //If message is of type OK
            {
                //recieved OK from callee
                okRequest r = new okRequest();
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Type".equals(feildName))
                        r.contentType = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Early-Media".equals(feildName))
                        r.pEarlyMedia = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Preferred-Identity".equals(feildName))
                        r.prefferedIdentity = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(feildName))
                    {
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                        break;
                    }
                }
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf("="));
                    if("v".equals(feildName))
                        r.v = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("o".equals(feildName))
                        r.o = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("s".equals(feildName))
                        r.s = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("c".equals(feildName))
                        r.c = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("t".equals(feildName))
                        r.t = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("m".equals(feildName))
                        r.m = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("a".equals(feildName))
                        r.a.add(nextLine.substring(nextLine.indexOf("=")+1,nextLine.length()));
                }
                
                //Find out fwd Ok to whom
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("OK forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward OK
                byte[] send = r.forwardOk(servIp).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
                
            }
            
            if("ACK".equals(typeOfMsg))     //If message is of type ACK
            {
                
                ackRequest r = new ackRequest();    //New ACK object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //whom to send this ack
                String fwdNumber = r.to.substring(r.to.indexOf(":")+1,r.to.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("ACK forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward ack
                byte[] send = r.forwardAck(line1, servIp, servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if("BYE".equals(typeOfMsg))     //If message is of type Bye
            {
                byeRequest r = new byeRequest();    //new Bye object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //whom to send this bye
                String fwdNumber = r.to.substring(r.to.indexOf(":")+1,r.to.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("BYE forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //Forward BYE
                byte[] send = r.forwardBye(line1, servIp, servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
                
                //remove this call from CURRENTCALLS
                String callId = r.callId.substring(0, r.callId.indexOf("@"));
                
                if(CURRENTCALLS.containsKey(callId))
                {
                    System.out.println("The call from "+CURRENTCALLS.get(callId).caller+" to "+CURRENTCALLS.get(callId).called+" has been ENDED.");
                    CURRENTCALLS.remove(callId);
                }
            }
            
            if("CANCEL".equals(typeOfMsg))      //If message is of type Cancel
            {
                cancelRequest r = new cancelRequest();  //new Cancel object
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //Find forward cancel to whom
                String fwdNumber = r.to.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Cancel forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward cancel
                byte[] send = r.forwardCancel(line1,servIp,servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
                
                //remove callDetails
                String callId = r.callId.substring(0,r.callId.indexOf("@"));
                if(CURRENTCALLS.containsKey(callId))
                    CURRENTCALLS.remove(callId);
            }
            
            if("SIP/2.0 487 Request Cancelled".equals(line1))   //If message is of type 487
            {
                requestTerminatedRequest r = new requestTerminatedRequest();
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //forward to whom
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Request Terminated forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward 487
                byte[] send = r.forwardrequestTerminated(line1,servIp,servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if(line1.contains("486 Busy"))  //If message is of type Busy
            {
                requestTerminatedRequest r = new requestTerminatedRequest();
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        r.via.add(nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length()));
                    else if("From".equals(feildName))
                        r.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        r.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        r.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        r.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        r.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        r.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        r.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                  
                    else if("User-Agent".equals(feildName))
                        r.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        r.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                   
                    else if("Content-Length".equals(feildName))
                        r.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                //forward to whom
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Busy Here forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward 486
                byte[] send = r.forwardrequestTerminated(line1,servIp,servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
                
                //remove from CURRENTCALLS
                String callId = r.callId.substring(0,r.callId.indexOf("@"));
                if(CURRENTCALLS.containsKey(callId))
                    CURRENTCALLS.remove(callId);
            }
           
                
            packet.setLength(ECHOMAX);
        
        }
            
    }

    
    private static String extractIpOrPort(String s,int choice) //Returns IP or PORT from SIP URI
    {
        if(choice == 0) //returns IP if choice == 0
        {
            return s.substring(0, s.indexOf(":"));
        }
        else //else returns PORT
            return s.substring(s.indexOf(":")+1);
    }
}