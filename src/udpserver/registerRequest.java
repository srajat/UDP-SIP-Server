package udpserver;

/**
 *
 * @author Rajat Saxena & Shivam Dabral & Biwas Bisht
 * @date 13/Jun/2016
 * @project UDP_Server
 * @File registerRequest.java
 */
public class registerRequest extends Request
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