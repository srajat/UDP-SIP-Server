package udpserver;

/**
 *
 * @author Rajat Saxena & Shivam Dabral & Biwas Bisht
 * @date 13/Jun/2016
 * @project UDP_Server
 * @File Request.java
 */

public class Request
{
    String via,from,to,callId,cSeq,contact,allow,maxForwards,
            userAgent,supported,contentLength;
    
    public Request()    //constructor
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
