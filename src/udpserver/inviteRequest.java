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
public class inviteRequest extends Request
{
    String contentType,pEarlyMedia,prefferedIdentity;
    String v,o,s,c,t,m;
    ArrayList<String> a,via;
    
    public inviteRequest()
    {
        super();
        this.contentType = "";
        this.pEarlyMedia = "";
        this.prefferedIdentity = "";
        
        this.v = "";
        this.c = "";
        this.m = "";
        this.o = "";
        this.s = "";
        this.t = "";
        
        a = new ArrayList<>();
        via = new ArrayList<>();
    }
    public String TRYING_100()
    {
        String trying_res = "SIP/2.0 100 TRYING\r\n";
        
        //add recieved to topmost via feild
        String upperViaFeild = via.get(0);
        String recieved = upperViaFeild.substring(upperViaFeild.indexOf(" ")+1, upperViaFeild.indexOf(":"));
        upperViaFeild = upperViaFeild + ";recieved=" + recieved;
        via.set(0, upperViaFeild);
        
        for(int in=0;in<via.size();in++)
            trying_res = trying_res + "Via: " + via.get(in) + "\r\n";
        
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
    
    public String forwardInvite(String line1,String servIp,int servPort)
    {
        String fwd_res = line1 + "\r\n";
        
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
        
        fwd_res = fwd_res + "Content-Type: " + contentType + "\r\n";
        //fwd_res = fwd_res + "Allow: " + allow + "\r\n";
        fwd_res = fwd_res + "Max-Forwards: " + (Integer.parseInt(maxForwards.trim())-1) + "\r\n";
        fwd_res = fwd_res + "User-Agent: " + userAgent + "\r\n";
        //fwd_res = fwd_res + "Supported: " + supported + "\r\n";
        fwd_res = fwd_res + "Content-Length: " + contentLength + "\r\n\r\n";
        
        fwd_res = fwd_res + "v=" + v + "\r\n";
        fwd_res = fwd_res + "o=" + o + "\r\n";
        fwd_res = fwd_res + "s=" + s + "\r\n";
        fwd_res = fwd_res + "c=" + c + "\r\n";
        fwd_res = fwd_res + "t=" + t + "\r\n";
        fwd_res = fwd_res + "m=" + m + "\r\n";
        
        for(int in=0;in<a.size();in++)
            fwd_res = fwd_res + "a=" + a.get(in) + "\r\n";
        
        return fwd_res;
    }
}
