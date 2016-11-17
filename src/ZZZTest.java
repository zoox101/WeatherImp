import java.io.IOException;
import java.util.Date;

public class ZZZTest {
	
	public static void main(String[] args) throws IOException {
		System.out.println(new Date() + " -- Rain Chance: ");
	}
	
	@SuppressWarnings("unused")
	private static void updateUsers(String username, String password) throws IOException {
		SMS.setGMailCredentials(username, password);
		CSVConverter allusers = new CSVConverter("WeatherImp Signup.csv");
		allusers.message("WeatherApp Update! Changelog: \n\n"
				+ "- Messages now come from weatherimp@gmail.com\n\n"
				+ "- Removed \"No rain today\" messages\n\n"
				+ "- Can now change notification times!\n\n"
				+ "To change times or add users, visit https://goo.gl/forms/ZU30b0NzVMn5jHXl2\n.");
	}
	
}
