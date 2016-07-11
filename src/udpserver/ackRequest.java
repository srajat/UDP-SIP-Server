package udpserver;
/**
 *
 * @author Rajat Saxena & Shivam Dabral & Biwas Bisht
 * @date 13/Jun/2016
 * @project UDP_Server
 * @File ackRequest.java
 */
import java.util.ArrayList;


public class ackRequest extends Request
{
    ArrayList<String> via;  //List of via headers
    
    ackRequest()    //constructor
    {
        super();
        via = new ArrayList<>();
    }
    
    public String forwardAck(String line1,String servIp,int servPort)
    {
        String fwd_res = line1 + "\r\n";
        
        //add recieved to topmost via feild
        String upperViaFeild = via.get(0);
        String recieved = upperViaFeild.substring(upperViaFeild.indexOf(" ")+1, upperViaFeild.indexOf(":"));
        upperViaFeild = upperViaFeild + ";recieved=" + recieved;
        via.set(0, upperViaFeild);
        
        //add this server's Via tag
        via.add(0,"SIP/2.0/UDP "+servIp+":"+servPort+";branch=z9hG4bK2d4790");
        
        //add via feilds
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
        
        fwd_res = fwd_res + "From: " + from + "\r\n";
        fwd_res = fwd_res + "To: " + to + "\r\n";
        fwd_res = fwd_res + "Call-ID: " + callId + "\r\n";
        fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        fwd_res = fwd_res + "Contact: " + contact + "\r\n";
        fwd_res = fwd_res + "Content-Length: 0\r\n\r\n";
 
        return fwd_res;
    }
}
