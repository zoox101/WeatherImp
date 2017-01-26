package ZZZTesting;
import java.io.IOException;
import javax.xml.soap.*;

public class SOAPRequest {
	
	public SOAPRequest(String url) throws UnsupportedOperationException, SOAPException, IOException {
		
		//Creating connection
		SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = factory.createConnection();
		
		SOAPMessage message = requestLatLon("74137");
		
		System.out.println(message.getSOAPBody().getFirstChild().getFirstChild().getTextContent());	
		
		SOAPMessage response = connection.call(message, url);
		message.writeTo(System.out); System.out.println(""); response.writeTo(System.out); System.out.println("");
		
		String textresponse = response.getSOAPBody().getFirstChild().getFirstChild().getTextContent();
		System.out.println(textresponse);
		
			
		System.out.println("Ending");
		connection.close();
	}
	
	public static void main(String[] args) throws SOAPException, IOException {
		new SOAPRequest("http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php");
	}
	
	public static SOAPMessage requestLatLon(String zipcode) throws SOAPException {
		 
		//Creating objects
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPBody body = message.getSOAPBody();
		
		//Editing message body
		message.getSOAPHeader().detachNode();
		SOAPElement llzip = body.addChildElement("LatLonListZipCode");
		SOAPElement ziplist = llzip.addChildElement("listZipCodeList").addTextNode(zipcode);
		ziplist.setAttribute("type", "xsd:string");
		
		//Returning message
		return message;
	}

}
