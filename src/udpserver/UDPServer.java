package udpserver;

/**
 *
 * @author Rajat Saxena
 * @date 8/Jun/2016
 * @project UDP_Server
 */
import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class UDPServer 
{
    private static final int ECHOMAX = 1023;

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
            byte[] arr = packet.getData();
            String request_msg = new String(arr);
            StringTokenizer st = new StringTokenizer(request_msg,"\r\n");
            
            /*while(st.hasMoreTokens())
            {
                System.out.println(st.nextToken());
                System.out.println();
            }*/
            
            //register_req r1 = new register_req();
            String req = "SIP/2.0 200 OK\r\n";
            while(st.hasMoreTokens())
            {
                req = req + st.nextToken() + "\r\n";
            }
            req = req + "\r\n";
            System.out.println("Handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
            System.out.println(req);


            //String register_response = r1.print();
            byte[] send = req.getBytes();
            DatagramPacket packet1 = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
            InetAddress clientAddress = packet.getAddress();
            packet1.setAddress(clientAddress);
            packet1.setPort(packet.getPort());
            packet1.setData(send);
            //System.out.println(register_response);
            socket.send(packet1);
            packet.setLength(ECHOMAX); 
        }
        /* NOT REACHED */
    }
}

/*class register_req
{
    String typeOfMsg,clientIp,serverIp,networkProtocol,
    branchNo,from,tag,to,callId;

    int cSeq,maxForwards,contentLength,contact,toNo,fromNo,serverPort;

    double sipVersion;

    register_req()
    {
        typeOfMsg="";
        clientIp="";
        serverIp="";
        networkProtocol="";
        branchNo="";
        from="";
        tag="";
        to="";
        callId="";

        cSeq=0;
        maxForwards=0;
        contentLength=0;
        contact=0;
        toNo=0;
        fromNo=0;
        serverPort=0;

        sipVersion=0.0;
    }
    String print()
    {
        String p;
        p = "SIP/"+sipVersion+" 200 OK\r\n"+
            "Via: SIP/"+sipVersion+"/"+networkProtocol+" "+clientIp+":"+serverPort+";branch="+branchNo+";recieved=192.0.2.201\r\n"+
            "From: "+from+" <sips:"+fromNo+"@"+serverIp+">;tag="+tag+"\r\n"+
            "To: "+to+" <sips:"+ toNo+"@"+serverIp+">\r\n"+
            "Call-ID: "+ callId+"@"+serverIp+"\r\n"+
            "CSeq: "+cSeq+" "+ typeOfMsg+"\r\n"+
            "Contact: <sips:"+from+"@"+clientIp+">;expires=7200"+"\r\n"+
            "Content-Length: "+contentLength+"\r\n\r\n";
        return p;
    }
}*/
