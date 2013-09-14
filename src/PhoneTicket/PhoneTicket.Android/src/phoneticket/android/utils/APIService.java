package phoneticket.android.utils;

public class APIService {
	
	static private String host = "https://192.168.1.36";
	static private int port = 44300;
	
	public static String baseAPIURL() {
		return host + ":" + port + "/API/";
	}
	
	public static String getRegisterUserPostURL() {
		return baseAPIURL() + "users";
	}

	public static String getAuthServicePostURL() {
		return baseAPIURL() + "users/auth";
	}

}
