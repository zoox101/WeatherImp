package ZZZTesting;
import javax.xml.soap.*;

import org.omg.CORBA.Request;

public class SOAPRequest {
	
	public SOAPRequest(String url) throws UnsupportedOperationException, SOAPException {
		//Creating connection
		SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = factory.createConnection();
		
		
		SOAPMessage response = connection.call(request(url), url);
		System.out.println(response);
		connection.close();
	}
	
	public static void main(String[] args) throws SOAPException {
		System.out.println(request("testing"));
	}
	
	public static SOAPMessage request(String server) throws SOAPException {
		
		//Creating objects
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPPart part = message.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
		SOAPBody body = envelope.getBody();
		
		//Specifying request
		envelope.addNamespaceDeclaration("TestingNameSpaceDeclaration", "http://ws.cdyne.com/");
		body.addChildElement("LatLonListZipCode", "TestingNameSpaceDeclaration");
		
		return message;
		
		
	}

}
