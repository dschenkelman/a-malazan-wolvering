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
		return baseAPIURL() + "movies";
	}

	public static String getRetrieveMovieGetURL(String movieID) {
		return getRetrieveMovieListServiceGetURL() + "/" + movieID;
	}

	public static String getRetrieveCinemaListServiceGetURL() {
		return baseAPIURL() + "complexes/";
	}

	public static String getRetrieveCinemaInfoServiceGetURL(int cinemaId) {
		return getRetrieveCinemaListServiceGetURL() + cinemaId;
	}

	public static String getRetrieveMovieFunctionsGetURL(String movieId) {
		return getRetrieveMovieGetURL(movieId) + "/weeklyShows";
	}

}
