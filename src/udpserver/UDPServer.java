package udpserver;

/**
 *
 * @author Rajat Saxena
 * @date 8/Jun/2016
 * @project UDP_Server
 */
import gov.nist.javax.sip.stack.MessageProcessor;
import java.net.*;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;
import org.apache.log4j.*;


public class UDPServer 
{
    private static final int ECHOMAX = 1023;
    
    
    public static void main(String[] args) throws IOException, PeerUnavailableException, TransportNotSupportedException, InvalidArgumentException, ObjectInUseException, TooManyListenersException 
    {
        String username;
        MessageProcessor messageProcessor;
        SipStack sipStack;
        SipFactory sipFactory;
        AddressFactory addressFactory;
        HeaderFactory headerFactory;
        MessageFactory messageFactory;
        SipProvider sipProvider;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the port of the server: ");
        int servPort = Integer.parseInt(br.readLine());
        
        DatagramSocket socket = new DatagramSocket(servPort) ;
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        username = "Server1";
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        Properties properties = new Properties();
	properties.setProperty("javax.sip.STACK_NAME", "rajat");
        properties.setProperty("javax.sip.IP_ADDRESS","127.0.0.1");
        //properties.setProperty("javax.sip.PATH_NAME","myStack");
        System.out.println(properties);
	sipStack = sipFactory.createSipStack(properties);
        headerFactory = sipFactory.createHeaderFactory();
	addressFactory = sipFactory.createAddressFactory();
	messageFactory = sipFactory.createMessageFactory();
        ListeningPoint udp = sipStack.createListeningPoint("127.0.0.1", 5060, "udp");
        sipProvider = sipStack.createSipProvider(udp);
        SipLayer sipLayer = new SipLayer();
	sipProvider.addSipListener(sipLayer);
        
        for (;;) 
        { 
            socket.receive(packet);
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            
            byte[] arr = packet.getData();
            String request_msg = new String(arr);           
                
            packet.setLength(ECHOMAX); 
        }
               
    }
        /* NOT REACHED */
}
    
class SipLayer implements SipListener 
{
    /**
     * This method uses the SIP stack to send a message. 
     */
    /*public void sendMessage(String to, String message) throws ParseException,
	    InvalidArgumentException, SipException {

	SipURI from = addressFactory.createSipURI(getUsername(), getHost()
		+ ":" + getPort());
	Address fromNameAddress = addressFactory.createAddress(from);
	fromNameAddress.setDisplayName(getUsername());
	FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
		"textclientv1.0");

	String username = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
	String address = to.substring(to.indexOf("@") + 1);

	SipURI toAddress = addressFactory.createSipURI(username, address);
	Address toNameAddress = addressFactory.createAddress(toAddress);
	toNameAddress.setDisplayName(username);
	ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

	SipURI requestURI = addressFactory.createSipURI(username, address);
	requestURI.setTransportParam("udp");

	ArrayList viaHeaders = new ArrayList();
	ViaHeader viaHeader = headerFactory.createViaHeader(getHost(),
		getPort(), "udp", "branch1");
	viaHeaders.add(viaHeader);

	CallIdHeader callIdHeader = sipProvider.getNewCallId();

	CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1,
		Request.MESSAGE);

	MaxForwardsHeader maxForwards = headerFactory
		.createMaxForwardsHeader(70);

	Request request = messageFactory.createRequest(requestURI,
		Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
		toHeader, viaHeaders, maxForwards);

	SipURI contactURI = addressFactory.createSipURI(getUsername(),
		getHost());
	contactURI.setPort(getPort());
	Address contactAddress = addressFactory.createAddress(contactURI);
	contactAddress.setDisplayName(getUsername());
	ContactHeader contactHeader = headerFactory
		.createContactHeader(contactAddress);
	request.addHeader(contactHeader);

	ContentTypeHeader contentTypeHeader = headerFactory
		.createContentTypeHeader("text", "plain");
	request.setContent(message, contentTypeHeader);

	sipProvider.sendRequest(request);
    }*/

    
    /** This method is called by the SIP stack when a response arrives. */
    public void processResponse(ResponseEvent evt) {
	Response response = evt.getResponse();
	int status = response.getStatusCode();

	System.out.println(response);
	
    }

    /** 
     * This method is called by the SIP stack when a new request arrives. 
     */
    public void processRequest(RequestEvent evt) {
	Request req = evt.getRequest();

	String method = req.getMethod();
	System.out.println(req);
    }


    /** 
     * This method is called by the SIP stack when there's no answer 
     * to a message. Note that this is treated differently from an error
     * message. 
     */
    public void processTimeout(TimeoutEvent evt) {
	//messageProcessor
		//.processError("Previous message not sent: " + "timeout");
    }

    /** 
     * This method is called by the SIP stack when there's an asynchronous
     * message transmission error.  
     */
    public void processIOException(IOExceptionEvent evt) {
	//messageProcessor.processError("Previous message not sent: "
		//+ "I/O Exception");
    }

    /** 
     * This method is called by the SIP stack when a dialog (session) ends. 
     */
    public void processDialogTerminated(DialogTerminatedEvent evt) {
    }

    /** 
     * This method is called by the SIP stack when a transaction ends. 
     */
    public void processTransactionTerminated(TransactionTerminatedEvent evt) {
    }

    /*public String getHost() {
	int port = sipProvider.getListeningPoint().getPort();
	String host = sipStack.getIPAddress();
	return host;
    }

    public int getPort() {
	int port = sipProvider.getListeningPoint().getPort();
	return port;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String newUsername) {
	username = newUsername;
    }

    public MessageProcessor getMessageProcessor() {
	return messageProcessor;
    }

    public void setMessageProcessor(MessageProcessor newMessageProcessor) {
	messageProcessor = newMessageProcessor;
    }*/

}