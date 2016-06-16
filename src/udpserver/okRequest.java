/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;

/**
 *
 * @author Administrator
 */
public class okRequest extends inviteRequest
{
    okRequest()
    {
        super();
    }
    
    public String forwardOk(String servIp)
    {
        String fwd_res = "SIP/2.0 200 OK\r\n";
        
        if(!cSeq.contains("BYE"))
        {
            String modifiedVia = via.get(0).substring(via.get(0).indexOf(",")+1,via.get(0).length());
            via.set(0,modifiedVia);
        }
        
        //add via feilds
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
        
        fwd_res = fwd_res + "From: " + from + "\r\n";
        fwd_res = fwd_res + "To: " + to + "\r\n";
        fwd_res = fwd_res+ "Call-ID: " + callId + "\r\n";
        fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        
        String modifiedContact = contact.substring(0,contact.indexOf("@")+1)+ servIp+">"; 
        fwd_res = fwd_res + "Contact: " + modifiedContact + "\r\n";
        
        //fwd_res = fwd_res + "Allow: " + allow + "\r\n";
        //fwd_res = fwd_res + "Max-Forwards: " + (Integer.parseInt(maxForwards.trim())-1) + "\r\n";
        //fwd_res = fwd_res + "User-Agent: " + userAgent + "\r\n";
        //fwd_res = fwd_res + "Supported: " + supported + "\r\n";
        fwd_res = fwd_res + "Content-Length: " + contentLength + "\r\n\r\n";
        
        if(!cSeq.contains("BYE"))
        {
            fwd_res = fwd_res + "v=" + v + "\r\n";
            fwd_res = fwd_res + "o=" + o + "\r\n";
            fwd_res = fwd_res + "s=" + s + "\r\n";
            fwd_res = fwd_res + "c=" + c + "\r\n";
            fwd_res = fwd_res + "t=" + t + "\r\n";
            fwd_res = fwd_res + "m=" + m + "\r\n";

            for(int in=0;in<a.size();in++)
                fwd_res = fwd_res + "a=" + a.get(in) + "\r\n";
        }
        
        return fwd_res;
    }
}
