/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;

import java.util.ArrayList;

/**
 *
 * @author bloodphoenix
 */
public class byeRequest extends Request
{
    ArrayList<String> via;
    byeRequest()
    {
        super();
        via = new ArrayList<>();
    }
    
    public String forwardBye(String line1,String servIp,int servPort)
    {
        String fwd_res = line1 + "\r\n";
        
        //add recieved to topmost via feild
        String upperViaFeild = via.get(0);
        String recieved = upperViaFeild.substring(upperViaFeild.indexOf(" ")+1, upperViaFeild.indexOf(":"));
        upperViaFeild = upperViaFeild + ";recieved=" + recieved;
        via.set(0, upperViaFeild);
        
        //add this server's Via tag
        via.add(0,"SIP/2.0/UDP "+servIp+":"+servPort+";branch=z9hG4bK2d4790");
        
        for(int in=0;in<via.size();in++)
            fwd_res = fwd_res + "Via: " + via.get(in) + "\r\n";
       
        
        fwd_res = fwd_res + "From: " + from + "\r\n";
        fwd_res = fwd_res + "To: " + to + "\r\n";
        fwd_res = fwd_res+ "Call-ID: " + callId + "\r\n";
        fwd_res = fwd_res + "CSeq: " + cSeq + "\r\n";
        
        String modifiedContact = contact.substring(0,contact.indexOf("@")+1)+ servIp+">"; 
        fwd_res = fwd_res + "Contact: " + modifiedContact + "\r\n";
        
        //fwd_res = fwd_res + "Content-Type: " + contentType + "\r\n";
        //fwd_res = fwd_res + "Allow: " + allow + "\r\n";
        fwd_res = fwd_res + "Max-Forwards: " + (Integer.parseInt(maxForwards.trim())-1) + "\r\n";
        fwd_res = fwd_res + "User-Agent: " + userAgent + "\r\n";
        //fwd_res = fwd_res + "Supported: " + supported + "\r\n";
        fwd_res = fwd_res + "Content-Length: " + contentLength + "\r\n\r\n";
        return fwd_res;
    }
}
