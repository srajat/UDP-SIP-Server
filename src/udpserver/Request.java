/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;

/**
 *
 * @author bloodphoenix
 */
public class Request
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
