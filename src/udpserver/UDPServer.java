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
import java.util.HashMap;
import java.util.StringTokenizer;

public class UDPServer
{
    private static final int ECHOMAX = 1024;
    private static HashMap<String,String> currentInvites = new HashMap<String,String>();
    
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
                
                byte[] send1 = i.RINGING_180().getBytes();
                DatagramPacket p1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                p1.setAddress(clientAddress);
                p1.setPort(clientPort);
                p1.setData(send1);
                socket.send(p1);
                
                if(!currentInvites.containsKey(i.to))
                {
                    System.out.print("Call coming from "+i.contact+" . 4 seconds to Pick up?? (y) or (n): ");
                    int wait = 4;
                    long startTime = System.currentTimeMillis();
                    while((System.currentTimeMillis() - startTime < wait*1000) && !br.ready()) {}
                    String pickup = "";
                    if(br.ready())
                    {
                        pickup = br.readLine();
                        if("y".equals(pickup))
                        {
                            //System.out.println(i.OK_200(line1, servPort));
                            byte[] send2 = i.OK_200(line1,servPort).getBytes();
                            DatagramPacket p2 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                            p2.setAddress(clientAddress);
                            p2.setPort(clientPort);
                            p2.setData(send2);
                            socket.send(p2);
                        }
                    }
                    else
                    {
                        System.out.println("Call not Picked up!!");
                    }
                }
                if(!currentInvites.containsKey(i.to))
                    currentInvites.put(i.to,i.cSeq);
                
                //for(int j=0;j<currentInvites.size();j++)
                    //System.out.println(currentInvites.get(j));
            }
            if("CANCEL".equals(typeOfMsg))
            {
                cancelRequest c = new cancelRequest();
                String feildName = "";
                String nextLine = "";
                while(st.hasMoreTokens())
                {
                    nextLine = st.nextToken();
                    feildName = nextLine.substring(0,nextLine.indexOf(":"));
                    if("Via".equals(feildName))
                        c.via = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("From".equals(feildName))
                        c.from = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("To".equals(feildName))
                        c.to = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Call-ID".equals(feildName))
                        c.callId = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("CSeq".equals(feildName))
                        c.cSeq = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Allow".equals(feildName))
                        c.allow = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Max-Forwards".equals(feildName))
                        c.maxForwards = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("User-Agent".equals(feildName))
                        c.userAgent = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                    else if("Content-Length".equals(feildName))
                        c.contentLength = nextLine.substring(nextLine.indexOf(" ")+1,nextLine.length());
                }
                
                String whoToCancel = line1.substring(line1.indexOf(" ")+1,line1.lastIndexOf(" "));
                whoToCancel = "<"+whoToCancel+">";
                
                if(currentInvites.containsKey(whoToCancel))
                {
                    
                    
                    //send ok back
                    //System.out.println(c.OK_200());
                    byte[] send = c.OK_200().getBytes();
                    DatagramPacket p = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                    p.setAddress(clientAddress);
                    p.setPort(clientPort);
                    p.setData(send);
                    socket.send(p);

                    //send 487 terminated
                    byte[] send1 = requestTerminatedResponse.REQUESTTERMINATED_487(c,currentInvites.get(whoToCancel)).getBytes();
                    DatagramPacket p1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                    p1.setAddress(clientAddress);
                    p1.setPort(clientPort);
                    p1.setData(send1);
                    socket.send(p1);
                    
                    currentInvites.remove(whoToCancel);
                }
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
        
        trying_res = trying_res + "Via: " + via + ";recieved="+ exractIp(c) +"\r\n";
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

    private String exractIp(String str) 
    {
        return str.substring(str.lastIndexOf(" ")+1, str.length());
    }
    
    public String RINGING_180()
    {
        String ring_res = "SIP/2.0 180 RINGING\r\n";
        
        ring_res = ring_res + "Via: " + via + ";recieved="+ exractIp(c) +"\r\n";
        ring_res = ring_res + "From: " + from + "\r\n";
        ring_res = ring_res + "To: " + to + "\r\n";
        ring_res = ring_res + "Call-ID: " + callId + "\r\n";
        ring_res = ring_res + "CSeq: " + cSeq + "\r\n";
        //ring_res = ring_res + "Contact: " + contact + "\r\n";
        ring_res = ring_res + "Allow: " + allow + "\r\n";
        //ring_res = ring_res + "Max-Forwards: " + maxForwards + "\r\n";
        ring_res = ring_res + "User-Agent: " + userAgent + "\r\n";
        ring_res = ring_res + "Supported: " + supported + "\r\n";
        ring_res = ring_res + "Content-Length: 0" + "\r\n";
        
        ring_res = ring_res + "\r\n";
        return ring_res;
    }
    public String OK_200(String line1,int servPort)
    {
        String ok_res = "SIP/2.0 200 OK\r\n";
        
        ok_res = ok_res + "Via: " + via + ";recieved="+ exractIp(c) +"\r\n";
        ok_res = ok_res + "From: " + from + "\r\n";
        ok_res = ok_res + "To: " + to + "\r\n";
        ok_res = ok_res + "Call-ID: " + callId + "\r\n";
        ok_res = ok_res + "CSeq: " + cSeq + "\r\n";
        ok_res = ok_res + "Contact: " + extractContact(line1,servPort) + "\r\n";
        ok_res = ok_res + "Content-Type: " + contentType + "\r\n";
        ok_res = ok_res + "Allow: " + allow + "\r\n";
        //ok_res = ok_res + "Max-Forwards: " + maxForwards + "\r\n";
        ok_res = ok_res + "User-Agent: " + userAgent + "\r\n";
        ok_res = ok_res + "Supported: " + supported + "\r\n";
        
        String body_res = "";
        body_res = body_res + "v=" + v + "\r\n";
        body_res = body_res + "o=" + owner(line1,o) + "\r\n";
        body_res = body_res + "s=" + s + "\r\n";
        body_res = body_res + "c=" + connect(line1) + "\r\n";
        body_res = body_res + "t=" + t + "\r\n";
        body_res = body_res + "m=" + mediaAndAttribute(m,servPort) + "\r\n";
        int len = body_res.getBytes().length;
        
        ok_res = ok_res + "Content-Length: " +len+ "\r\n";  
        ok_res = ok_res + "\r\n";
        
        ok_res = ok_res + body_res;
        return ok_res;
    }

    private String extractContact(String line1, int servPort) {
        String ss = line1.substring(7,line1.lastIndexOf(" "));
        String sss = "<"+ss+":"+servPort+">";
        
        return sss;
    }

    private String owner(String line1,String o) {
        String ss = o.substring(0, o.lastIndexOf(" "));
        ss = ss + " ";
        ss = ss + line1.substring(line1.indexOf("@")+1,line1.lastIndexOf(" "));
        
        return ss;
        
    }

    private String connect(String line1) {
        return "IN IP4 " + line1.substring(line1.indexOf("@")+1,line1.lastIndexOf(" "));
    }

    private String mediaAndAttribute(String m, int servPort) {
        String ss = "audio " + (servPort+3) + " ";
        ss = ss + m.substring(m.indexOf("RTP"), m.length()) + "\r\n";
        
        for(int i=0;i<a.size();i++)
        {
            ss = ss + "a=";
            ss = ss + a.get(i);
            ss = ss + "\r\n";
        }
        return ss;
    }

}

class cancelRequest extends Request
{
    cancelRequest()
    {
        super();
    }
    
    public String OK_200()
    {
        String ok_res = "SIP/2.0 200 OK\r\n";
        
        ok_res = ok_res + "Via: " + via + "\r\n";
        ok_res = ok_res + "From: " + from + "\r\n";
        ok_res = ok_res + "To: " + to + "\r\n";
        ok_res = ok_res + "Call-ID: " + callId + "\r\n";
        ok_res = ok_res + "CSeq: " + cSeq + "\r\n";
        ok_res = ok_res + "Allow: " + allow + "\r\n";
        ok_res = ok_res + "Max-Forwards: " + maxForwards + "\r\n";
        ok_res = ok_res + "User-Agent: " + userAgent + "\r\n";
        ok_res = ok_res + "Content-Length: " + contentLength + "\r\n";
        
        ok_res = ok_res + "\r\n";
        return ok_res;
    }
}

class requestTerminatedResponse
{
    public static String REQUESTTERMINATED_487(cancelRequest c,String inviteSeq)
    {
        String term_res = "SIP/2.0 487 Request Terminated\r\n";
        
        term_res = term_res + "Via: " + c.via + "\r\n";
        term_res = term_res + "From: " + c.from + "\r\n";
        term_res = term_res + "To: " + c.to + "\r\n";
        term_res = term_res + "Call-ID: " + c.callId + "\r\n";
        term_res = term_res + "CSeq: " + inviteSeq + "\r\n";
        
        term_res = term_res + "Allow: " + c.allow + "\r\n";
        term_res = term_res + "Max-Forwards: " + c.maxForwards + "\r\n";
      
        term_res = term_res + "User-Agent: " + c.userAgent + "\r\n";
        
        
        term_res = term_res + "Content-Length: 0\r\n";
        
        term_res = term_res + "\r\n";
        return term_res;
    }
}