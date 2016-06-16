package udpserver;

/**
 *
 * @author Rajat Saxena
 * @date 13/Jun/2016
 * @project UDP_Server
 */

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UDPServer
{
    private static final int ECHOMAX = 2048;
    private static HashMap<String,String> REGISTERED = new HashMap<String,String>();
    private static HashMap<String,callDetails> CURRENTCALLS = new HashMap<String,callDetails>();
    
    public static void main(String[] args) throws IOException
    {           
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the port of the server: ");
        
        int servPort = Integer.parseInt(br.readLine());
        String servIp = "192.168.43.23";
        //String servIp = "172.210.140.251";
        
        DatagramSocket socket = new DatagramSocket(servPort) ;
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
       
            
        for (;;)
        {
            socket.receive(packet);
            
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            
            byte[] arr = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), arr, 0, arr.length);
            String requestMsg = new String(arr);
            
            //System.out.println(request_msg);
            StringTokenizer st = new StringTokenizer(requestMsg,"\r\n");
            String line1 = st.nextToken();
            String typeOfMsg = line1.substring(0, line1.indexOf(" "));
            
            if("REGISTER".equals(typeOfMsg))
            {
                registerRequest r = new registerRequest();
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
                
                if(!isRegistered && expires > 0)
                {
                    System.out.println("Phone "+number+" is Successfully Registered at IP:PORT "+ipPort+" .");
                    REGISTERED.put(number, ipPort);
                }
                    
                else if(isRegistered && expires == 0)
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
            
            if("INVITE".equals(typeOfMsg))
            {
                inviteRequest r = new inviteRequest();
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
                cd.cSeq = r.cSeq;
                String callId = r.callId.substring(0, r.callId.indexOf("@"));
                CURRENTCALLS.put(callId, cd);
                
            }
            
            if("SIP/2.0 180 Ringing".equals(line1))
            {
                ringingRequest r = new ringingRequest();
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
                
                //forward ringing to the caller
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Ringing forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward ringing
                //System.out.println(r.forwardRinging());
                byte[] send = r.forwardRinging(servIp).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if("SIP/2.0 200 OK".equals(line1))
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
                
                //fwd Ok to whom
                String fwdNumber = r.from.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("OK forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward OK
                //System.out.println(r.forwardOk(servIp));
                byte[] send = r.forwardOk(servIp).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
                
            }
            
            if("ACK".equals(typeOfMsg))
            {
                System.out.println("ACK RECIEVED");
                
                ackRequest r = new ackRequest();
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
                //System.out.println(r.forwardAck(line1, servIp, servPort));
                byte[] send = r.forwardAck(line1, servIp, servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if("BYE".equals(typeOfMsg))
            {
                byeRequest r = new byeRequest();
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
                //System.out.println(r.forwardBye(line1, servIp, servPort));
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
            
            if("CANCEL".equals(typeOfMsg))
            {
                cancelRequest r = new cancelRequest();
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
                String fwdNumber = r.to.substring(r.from.indexOf(":")+1, r.from.indexOf("@"));
                String fwdIp = extractIpOrPort(REGISTERED.get(fwdNumber),0);
                String fwdPort = extractIpOrPort(REGISTERED.get(fwdNumber),1);
                
                System.out.println("Cancel forwarded to "+fwdNumber+" at IP:PORT "+fwdIp+":"+fwdPort+" .");
                
                //forward cancel
                //System.out.println(r.forwardCancel());
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
            
            if("SIP/2.0 487 Request Terminated".equals(line1))
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
                //System.out.println(r.forwardrequestTerminated());
                byte[] send = r.forwardrequestTerminated(line1,servIp,servPort).getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(InetAddress.getByName(fwdIp));
                p.setPort(Integer.parseInt(fwdPort.trim()));
                p.setData(send);
                socket.send(p);
            }
            
            if("SIP/2.0 486 Busy here".equals(line1))
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
                //System.out.println(r.forwardrequestTerminated());
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