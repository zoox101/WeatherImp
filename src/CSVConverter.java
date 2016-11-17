import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVConverter extends ArrayList<User> {
	private static final long serialVersionUID = 1L;

	public CSVConverter(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		reader.readLine();
		String nextline;
		while((nextline = reader.readLine()) != null) {
			String[] split = nextline.split(",");
			this.add(new User(cut(split[1]), cut(split[2]), cut(split[3]), cut(split[4])));
		}
		reader.close();
	}
	
	private String cut(String string) {
		 string = string.substring(1);
		 string = string.substring(0, string.length()-1);
		 return string;
	}
	
	public void message(String message) {
		for(User user: this)
			user.message(message);
	}
	
	@Override
	public String toString() { 
		String returnstring = "";
		for(User user: this)
			returnstring += user + "\n";
		return returnstring;
	}
	
}
