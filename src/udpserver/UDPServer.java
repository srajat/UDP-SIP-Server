package udpserver;

/**
 *
 * @author Rajat Saxena
 * @date 8/Jun/2016
 * @project UDP_Server
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UDPServer
{
    private static final int ECHOMAX = 1024;
    
    public static void main(String[] args) throws IOException
    {           
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the port of the server: ");
        int servPort = Integer.parseInt(br.readLine());
        
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
                
                byte[] send = r.OK_200().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
            }
            else if("INVITE".equals(typeOfMsg))
            {
                inviteRequest i = new inviteRequest();
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    
                    if("Via".equals(feildName))
                        i.via = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("From".equals(feildName))
                        i.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        i.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        i.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        i.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Contact".equals(feildName))
                        i.contact = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Type".equals(feildName))
                        i.contentType = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        i.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        i.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Supported".equals(feildName))
                        i.supported = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Early-Media".equals(feildName))
                        i.pEarlyMedia = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(feildName))
                        i.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("P-Preferred-Identity".equals(feildName))
                        i.prefferedIdentity = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(feildName))
                    {
                        i.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                        break;
                    }
                }
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf("="));
                    if("v".equals(feildName))
                        i.v = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("o".equals(feildName))
                        i.o = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("s".equals(feildName))
                        i.s = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("c".equals(feildName))
                        i.c = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("t".equals(feildName))
                        i.t = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("m".equals(feildName))
                        i.m = nextLine.substring(nextLine.indexOf("=")+1,nextLine.length());
                    else if("a".equals(feildName))
                        i.a.add(nextLine.substring(nextLine.indexOf("=")+1,nextLine.length()));
                }
                
                //System.out.println(i.TRYING_100());
                byte[] send = i.TRYING_100().getBytes();
                DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p.setAddress(clientAddress);
                p.setPort(clientPort);
                p.setData(send);
                socket.send(p);
            }
            
            packet.setLength(ECHOMAX);
        }
    }
    
}
class Request
{
    String via,from,to,callId,cSeq,contact,allow,maxForwards,
            userAgent,supported,contentLength;
    
    public Request(String via,String from,String to,String callId,String cSeq,String contact,String allow
    ,String maxForwards,String userAgent,String supported,String contentLength)
    {
        this.via = via;
        this.from = from;
        this.to = to;
        this.callId = callId;
        this.cSeq = cSeq;
        this.contact = contact;
        this.allow = allow;
        this.maxForwards = maxForwards;
        this.userAgent = userAgent;
        this.supported = supported;
        this.contentLength = contentLength;
    }
    public Request()
    {
        this.allow = "";
        this.cSeq = "";
        this.callId = "";
        this.contact = "";
        this.contentLength = "";
        this.from = "";
        this.maxForwards = "";
        this.supported = "";
        this.to = "";
        this.userAgent = "";
        this.via = "";
    }
}

class registerRequest extends Request
{
    String allowEvents,expires;
    
    public registerRequest(String via,String from,String to,String callId,String cSeq,String contact,String allow
    ,String maxForwards,String allowEvents,String userAgent,String supported,String expires,
    String contentLength)
    {
        this.via = via;
        this.from = from;
        this.to = to;
        this.callId = callId;
        this.cSeq = cSeq;
        this.contact = contact;
        this.allow = allow;
        this.maxForwards = maxForwards;
        this.allowEvents = allowEvents;
        this.userAgent = userAgent;
        this.supported = supported;
        this.expires = expires;
        this.contentLength = contentLength;
    }
    public registerRequest()
    {
        super();
        this.allowEvents = "";
        this.expires = "";      
    }
    public String OK_200()
    {
        String ok_res = "SIP/2.0 200 OK\r\n";
        
        ok_res = ok_res + "Via: " + via + "\r\n";
        ok_res = ok_res + "From: " + from + "\r\n";
        ok_res = ok_res + "To: " + to + "\r\n";
        ok_res = ok_res + "Call-ID: " + callId + "\r\n";
        ok_res = ok_res + "CSeq: " + cSeq + "\r\n";
        ok_res = ok_res + "Contact: " + contact + "\r\n";
        ok_res = ok_res + "Allow: " + allow + "\r\n";
        ok_res = ok_res + "Max-Forwards: " + maxForwards + "\r\n";
        ok_res = ok_res + "Allow-Events: " + allowEvents + "\r\n";
        ok_res = ok_res + "User-Agent: " + userAgent + "\r\n";
        ok_res = ok_res + "Supported: " + supported + "\r\n";
        ok_res = ok_res + "Expires: " + expires + "\r\n";
        ok_res = ok_res + "Content-Length: " + contentLength + "\r\n";
        
        ok_res = ok_res + "\r\n";
        return ok_res;
    }
}

class inviteRequest extends Request
{
    String contentType,pEarlyMedia,prefferedIdentity,contentLength;
    String v,o,s,c,t,m;
    ArrayList a;
    
    public inviteRequest()
    {
        super();
        this.contentType = "";
        this.contentLength = "";
        this.pEarlyMedia = "";
        this.prefferedIdentity = "";
        
        this.v = "";
        this.c = "";
        this.m = "";
        this.o = "";
        this.s = "";
        this.t = "";
        
        a = new ArrayList();
    }
    public String TRYING_100()
    {
        String trying_res = "SIP/2.0 100 TRYING\r\n";
        
        trying_res = trying_res + "Via: " + via + ";recieved=192.168.43.81" +"\r\n";
        trying_res = trying_res + "From: " + from + "\r\n";
        trying_res = trying_res + "To: " + to + "\r\n";
        trying_res = trying_res + "Call-ID: " + callId + "\r\n";
        trying_res = trying_res + "CSeq: " + cSeq + "\r\n";
        //trying_res = trying_res + "Contact: " + contact + "\r\n";
        trying_res = trying_res + "Allow: " + allow + "\r\n";
        //trying_res = trying_res + "Max-Forwards: " + maxForwards + "\r\n";
        trying_res = trying_res + "User-Agent: " + userAgent + "\r\n";
        trying_res = trying_res + "Supported: " + supported + "\r\n";
        trying_res = trying_res + "Content-Length: 0" + "\r\n";
        
        trying_res = trying_res + "\r\n";
        return trying_res;
    }
}