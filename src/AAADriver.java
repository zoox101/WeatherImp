import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;

public class AAADriver {
	
	public final static double VERSION = 1.13;
	public static Writer writer = (new AAADriver()).new Writer();
	
	public static void main(String[] args) throws ParseException, IOException {
		
		AAADriver.writer.printf("\nStarting WeatherApp Version %.2f\n", VERSION);
		SMS.setGMailCredentials(args[0], args[1]);
		
		CSVConverter allusers = new CSVConverter("WeatherImp Signup.csv");
		ArrayList<TimeGroup> timegroups = new ArrayList<TimeGroup>();
		SMS ADMIN = allusers.get(0); 
		ADMIN.message("Server reboot complete. Version: " + VERSION);	
		writer.println("Messaging Admin: " + ADMIN.toString());
		writer.println("Admin message sent...");
		
		for(User user: allusers) {
			boolean hasgroup = false;
			for(TimeGroup timegroup: timegroups)
				if(timegroup.time.equals(user.notificationtime))
					hasgroup = timegroup.add(user);
			if(!hasgroup) {
				timegroups.add(new TimeGroup(user.notificationtime));
				timegroups.get(timegroups.size()-1).add(user);
			}
		}
		
		for(TimeGroup timegroup: timegroups)
			timegroup.scheduleEvent();	
		AAADriver.writer.println("Timers Set. Closing main thread.");
	}
	
	private AAADriver(){}
	
	public class Writer {
		PrintWriter writer;
		
		private Writer() {
			try {this.writer = new PrintWriter(new FileOutputStream(new File("ServerLog.txt"), true));} 
			catch (FileNotFoundException e) {e.printStackTrace();}
		}
		
		public void println(String string) {
			System.out.println(string);
			writer.println(string);
			writer.flush();
		}
		
		public void print(String string) {
			System.out.print(string);
			writer.print(string);
			writer.flush();
		}
		
		public void printf(String string, Object args) {
			System.out.printf(string, args);
			writer.printf(string, args);
			writer.flush();
		}
	}

}
