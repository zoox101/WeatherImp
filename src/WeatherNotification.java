import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class WeatherNotification extends TimerTask {
	
	public final static int PRECIP_THRESHOLD = 35; //35
	public final static int ZIPCODE = 73019;
	public ArrayList<User> contacts;
	
	public WeatherNotification(TimeGroup timegroup) throws IOException {
		this.contacts = timegroup;
	}
	
	@Override
	public void run() {
		ArrayList<String> html;
		AAADriver.writer.print("Running Process: ");
		try {
			html = connectToServer();
			message(contacts, getRain(html));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	protected ArrayList<String> connectToServer() throws IOException {
		URL url = new URL(getURL());
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		ArrayList<String> returnstrings = new ArrayList<String>();
		String singleline;
		while((singleline = reader.readLine()) != null) {
			returnstrings.add(singleline.trim());
		}
		return returnstrings;
	}
	
	protected String getURL() {
		return "http://graphical.weather.gov/xml/sample_products/browser_"
				+ "interface/ndfdBrowserClientByDay.php?whichClient=NDFDg"
				+ "enByDayMultiZipCode&lat=&lon=&listLatLon=&lat1=&lon1=&"
				+ "lat2=&lon2=&resolutionSub=&endPoint1Lat=&endPoint1Lon="
				+ "&endPoint2Lat=&endPoint2Lon=&centerPointLat=&centerPoi"
				+ "ntLon=&distanceLat=&distanceLon=&resolutionSquare=&zip"
				+ "CodeList=" + ZIPCODE + "&citiesLevel=&format=24+hourly"
				+ "&startDate=time&numDays=1&Unit=e&Submit=Submit";
	}
	
	protected int[] getRain(ArrayList<String> html) {
		int value = 0;
		for(int i=0; i<html.size(); i++)
			if(html.get(i).equals("<name>12 Hourly Probability of Precipitation</name>"))
				value = i;
		int rain[] = new int[2];
		rain[0] = cut(html.get(value+1));
		rain[1] = cut(html.get(value+2));
		AAADriver.writer.println(new Date() + " -- Rain Chance: " + rain[0] + " - " + rain[1]);
		return rain;
	}
	
	private static int cut(String string) {
		//<value>10</value>
		String newstring = "";
		if(string.length() == 16) newstring = string.substring(7,8);
		if(string.length() == 17) newstring = string.substring(7,9);
		if(string.length() == 18) newstring = string.substring(7,10);
		return Integer.parseInt(newstring);
	}
	
	protected void message(ArrayList<User> contacts, int[] rain) {
		int maxrainchance = Integer.max(rain[0], rain[1]);
		if(rain[0] > PRECIP_THRESHOLD || rain[1] > PRECIP_THRESHOLD)
			for(SMS contact: contacts)
				contact.message(maxrainchance + "% chance of rain today.");
	}
	
}
