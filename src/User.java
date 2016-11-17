
public class User extends SMS {
	
	public String name;
	public String notificationtime;
	
	public User(String name, String phonenumber, String carrier, String notificationtime) {
		super(phonenumber, getCarrier(carrier));
		this.name = name;
		this.notificationtime = notificationtime;
	}
	
	private static SMS.Carrier getCarrier(String carrier) { 
		if(carrier.equals("AT&T")) return SMS.Carrier.ATT;
		if(carrier.equals("TMobile")) return SMS.Carrier.TMOBILE;
		if(carrier.equals("Sprint")) return SMS.Carrier.SPRINT;
		if(carrier.equals("Verizon")) return SMS.Carrier.VERIZON;
		return null;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
