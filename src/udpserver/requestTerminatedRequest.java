package udpserver;

import java.util.ArrayList;

/**
 *
 * @author Rajat Saxena & Shivam Dabral & Biwas Bisht
 * @date 13/Jun/2016
 * @project UDP_Server
 * @File requestTerminatedRequest.java
 */
public class requestTerminatedRequest extends Request
{
    ArrayList<String> via;
    requestTerminatedRequest()
    {
        super();
        via = new ArrayList<>();
    }
    public String forwardrequestTerminated(String line1,String servIp,int servPort)
    {
        String fwd_res = line1 + "\r\n";
        
        if(via.get(0).contains(","))
        {
            String modifiedVia = via.get(0).substring(via.get(0).indexOf(",")+1,via.get(0).length());
            via.set(0,modifiedVia);
        }
        else
            via.remove(0);
        
        
        //add via feilds
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
        
        fwd_res = fwd_res + "From: " + from + "\r\n";
        fwd_res = fwd_res + "To: " + to + "\r\n";
        fwd_res = fwd_res+ "Call-ID: " + callId + "\r\n";
        fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        
        String modifiedContact = contact.substring(0,contact.indexOf("@")+1)+ servIp+">"; 
        fwd_res = fwd_res + "Contact: " + modifiedContact + "\r\n";
        
        fwd_res = fwd_res + "User-Agent: " + userAgent + "\r\n";
        
        fwd_res = fwd_res + "Content-Length: 0" + "\r\n\r\n";
        
        return fwd_res;
    }
}
