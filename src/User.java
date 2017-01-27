
public class User extends SMS {
	
	public String name;
	public String notificationtime;
	public String zipcode;
	
	public User(String name, String phonenumber, String carrier, String zipcode, String notificationtime) {
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
	public String toString() {return this.name;}
	
	@Override
	//Returns true if the two users are in the same zipcode and get notifications at the same time
	public boolean equals(Object object) {
		if(object instanceof User) {
			User that = (User) object;
			boolean sametime = this.notificationtime.equals(that.notificationtime);
			boolean sameplace = this.zipcode.equals(that.zipcode);
			if(sametime && sameplace) return true;
		}
		return false;
	}

}
