import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author WilliamBooker
 * @version 1.0
 * @description A simple class allowing you to send free text messages from a java runtime. 
 * 
 * This program uses a GMail account to forward messages to carriers' free Email-SMS services.
 * Messaging requires advance knowledge of the recipient's cell-carrier. Active GMail account required. 
 * GMail security settings my need to be altered to allow this application access. Use at your own risk.
 * 
 * The GMail proxy is set using the static method setGMailCredentials(String username, String password).
 * The SMS constructor takes in a phone number and a carrier. The carrier enum is defined in SMS.
 * The instance method message(String message) sends the actual SMS.
 * 
 * Example Message:
 * SMS.setGMailCredentials("MyUsername", "MyPassword");
 * SMS phone = new SMS("5555555555", SMS.Carrier.ATT);
 * phone.message("MyMessage");
 */

public class SMS {
	
	protected static String USERNAME;
	protected static String PASSWORD;
	
	public static enum Carrier {ATT, TMOBILE, SPRINT, VERIZON};
	protected String phonenumber;
	protected Carrier carrier;
	
	
	/**
	 * Constructor that takes in the desired recipient
	 * @param phonenumber -- The phone number to message
	 * @param carrier -- The carrier for that phone number
	 */
	public SMS(String phonenumber, Carrier carrier) {
		this.phonenumber = phonenumber;
		this.carrier = carrier;
	}
	
	/**
	 * Static method that sets the GMail proxy
	 * @param username -- The user name of the GMail account to be used
	 * @param password -- The password of the GMail account to be used
	 */
	public static void setGMailCredentials(String username, String password) {
		SMS.USERNAME = username;
		SMS.PASSWORD = password;
	}
	
	/**
	 * Sends the message to the desired recipient
	 * @param message -- The message to be sent 
	 * @return Boolean value indicating if the message sent successfully
	 */
	public boolean message(String message) {
		
		//Setting mail properties
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        //Creating the authenticator
        Authenticator authenticator = new Authenticator() { 
        	protected PasswordAuthentication getPasswordAuthentication(){
        		return new PasswordAuthentication(USERNAME, PASSWORD);
        	}
        };
        Session session = Session.getInstance(props, authenticator);
        
        //Attempting to send message
        try {
        	//Creating message
        	javax.mail.Message email = new MimeMessage(session);
        	//Sending email to carrier's Email-SMS service
        	email.setRecipients(javax.mail.Message.RecipientType.TO,
        			InternetAddress.parse(phonenumber + getCarrier(carrier)));
        	email.setText(message);
        	//Sending message
        	Transport.send(email);
        	return true;
        } catch(MessagingException error) {
        	System.out.print("A messaging error occured.\n");
        	//If the user failed to enter a valid GMail username or password...
        	if(USERNAME == null || PASSWORD == null)
        		System.out.println("Unspecified username or password. Set GMail proxy with SMS.setGMailCredentials(username, password).");
        	error.printStackTrace();
        	return false;
        }
		
	}
	
	/**
	 * Takes in a carrier and returns the correct email address
	 * @param carrier -- The carrier to be used
	 * @return A string representing the carrier's email address
	 */
	protected static String getCarrier(Carrier carrier) {
		if(carrier == Carrier.ATT) return "@txt.att.net";
		if(carrier == Carrier.TMOBILE) return "@tmomail.net";
		if(carrier == Carrier.SPRINT) return "@messaging.sprintpcs.com";
		if(carrier == Carrier.VERIZON) return "@vtext.com";
		return "";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Converts the object to a String
	 * Ex: (555) 555-5555 at VERIZON
	 */
	public String toString() {
		return numberToString(phonenumber) + " at " + carrier;
	}
	
	
	/**
	 * Takes in a phonenumber and converts it into a standard form
	 * @param phonenumber
	 * @return a converted string in the form (555) 555-5555
	 */
	public static String numberToString(String phonenumber) {
		String outputnumber = "(";
		outputnumber += phonenumber.charAt(0);
		outputnumber += phonenumber.charAt(1);
		outputnumber += phonenumber.charAt(2);
		outputnumber += ") ";
		outputnumber += phonenumber.charAt(3);
		outputnumber += phonenumber.charAt(4);
		outputnumber += phonenumber.charAt(5);
		outputnumber += "-";
		outputnumber += phonenumber.charAt(6);
		outputnumber += phonenumber.charAt(7);
		outputnumber += phonenumber.charAt(8);
		outputnumber += phonenumber.charAt(9);
		return outputnumber;
	}

}
