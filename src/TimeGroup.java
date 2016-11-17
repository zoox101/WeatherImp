import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class TimeGroup extends ArrayList<User> {
	private static final long serialVersionUID = 1L;
	
	public static final int MILLISECONDSPERDAY = 86400000;
	public String time;
	
	/**
	 * Creates a new TimeGroup at a given time
	 * @param time -- The time the notification goes off at
	 */
	public TimeGroup(String time) {
		this.time = time;
	};
	
	/**
	 * Creates the timer and sets the daily repeating event
	 * @throws IOException
	 * @throws ParseException
	 */
	public void scheduleEvent() throws IOException, ParseException {		
		Timer timer = new Timer();
		timer.schedule(new WeatherNotification(this), getDate(time), MILLISECONDSPERDAY);
	}
	
	/**
	 * Returns a date object that tells scheduleEvent when to schedule the first event
	 * @param time -- The time the notification is supposed to go off
	 * @return a date object giving the time for the first notification
	 * @throws ParseException
	 */
	public static Date getDate(String time) throws ParseException {
		
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Date scheduletime = formatter.parse(time);
		scheduletime = new Date(scheduletime.getTime()%MILLISECONDSPERDAY);
		Date now = new Date();
		
		Date scheduledate = new Date(now.getTime() - (now.getTime()+3600000)%MILLISECONDSPERDAY + scheduletime.getTime());
		if(scheduledate.before(now)) scheduledate = new Date(scheduledate.getTime() + 86400000);
		AAADriver.writer.println("Setting schedule date to " + scheduledate);
		return scheduledate;
	}
	
	@Override
	public String toString() {
		String returnstring = "[";
		returnstring += time + ": ";
		for(int i=0; i<this.size()-1; i++)
			returnstring += this.get(i) + ", ";
		returnstring += this.get(this.size()-1) + "]";
		return returnstring;
	}
	
	
	

}
