
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class WeatherRequest {
	private static HashMap<String, String[]> latlon = new HashMap<String, String[]>();


	public static void main(String[] args) throws Exception {
		String[] latlon = {"36.1277", "-95.9164"};
		new WeatherRequest(latlon);
		
	}
	
	public WeatherRequest(String[] latlon) throws Exception {
		requestWeather(latlon, getCurrentDates());
	}

	public WeatherRequest(String zipcode) throws Exception {
		requestWeather(requestLatLon(zipcode), getCurrentDates());
	}

	private static String[] getCurrentDates() {
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + 86400000);
		DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");
		String[] dates = {formatter.format(today), formatter.format(tomorrow)};
		return dates;
	}
	
	//Gets the weather for a given lat-lon pair
	private void requestWeather(String[] latlon, String[] times) throws Exception {

		//Creating connection
		String url = "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php";
		SOAPConnectionFactory soapfactory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = soapfactory.createConnection();
		
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPBody body = message.getSOAPBody();
		SOAPElement ndfdgen = body.addChildElement("NDFDgen");
		message.getSOAPHeader().detachNode();

		//Adding relevant information
		newStringElement(ndfdgen, "latitude", latlon[0]);
		newStringElement(ndfdgen, "longitude", latlon[1]);
		newStringElement(ndfdgen, "product", "time-series");
		newStringElement(ndfdgen, "startTime", times[0]);
		newStringElement(ndfdgen, "endTime", times[1]);
			
		//Getting response from server
		SOAPMessage response = connection.call(message, url);
		String textresponse = response.getSOAPBody().getFirstChild().getFirstChild().getTextContent();
		connection.close();

		message.writeTo(System.out); System.out.println("");
		response.writeTo(System.out);
		System.out.println("\n" + textresponse);
	}
	
	//Helper method for requestWeather. Creates a new string-encoded SOAP element. 
	private void newStringElement(SOAPElement parent, String name, String value) throws SOAPException {
		SOAPElement element = parent.addChildElement(name).addTextNode(value);
		element.setAttribute("type", "xsd:string");	
	}

	//Gets the latitude and longitude for a zipcode from the NWS
	private String[] requestLatLon(String zipcode) throws Exception {

		//Creating connection
		String url = "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php";
		SOAPConnectionFactory soapfactory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = soapfactory.createConnection();

		//Creating SOAP objects
		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		SOAPBody body = message.getSOAPBody();

		//Editing message body
		message.getSOAPHeader().detachNode();
		SOAPElement llzip = body.addChildElement("LatLonListZipCode");
		SOAPElement ziplist = llzip.addChildElement("listZipCodeList").addTextNode(zipcode);
		ziplist.setAttribute("type", "xsd:string");

		//Getting response from server
		SOAPMessage response = connection.call(message, url);
		String textresponse = response.getSOAPBody().getFirstChild().getFirstChild().getTextContent();
		connection.close();

		//Parsing xml document
		DocumentBuilderFactory xmlfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = xmlfactory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new ByteArrayInputStream(textresponse.getBytes("utf-8"))));
		document.getChildNodes();
		String latlon = document.getFirstChild().getFirstChild().getTextContent();

		//Returning latitude and longitude as an array of strings
		WeatherRequest.latlon.put(zipcode, latlon.split(","));
		return latlon.split(",");
	}

}
