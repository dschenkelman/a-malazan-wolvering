package phoneticket.android.utils;

public class APIService {

	static private String host = "https://phoneticket.apphb.com/";

	public static String baseAPIURL() {
		return host + "/API/";
	}

	public static String getRegisterUserPostURL() {
		return baseAPIURL() + "users";
	}

	public static String getAuthServicePostURL() {
		return baseAPIURL() + "users/auth";
	}

	public static String getRetrieveMovieListServiceGetURL() {
		return baseAPIURL() + "movielist";
	}

	public static String getRetrieveMovieGetURL(String movieID) {
		return getRetrieveMovieListServiceGetURL() + "/" + movieID;
	}

}
