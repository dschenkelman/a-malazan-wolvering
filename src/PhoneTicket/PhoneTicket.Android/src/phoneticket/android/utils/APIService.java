package phoneticket.android.utils;

public class APIService {

	static private String host = "https://phoneticket.apphb.com"; //"https://Damian-PC:44300";//

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

	public static String getRetrieveRoomInfoServiceGetURL(String functionId) {
		return baseAPIURL() + "shows" + "/" + functionId + "/seats";

	}

	public static String getRetrieveDiscountsListServiceGetURL() {
		return baseAPIURL() + "discounts";
	}

	public static String getRegisterPurchasePostURL() {
		return baseAPIURL() + "purchases";
	}

	public static String getRegisterReservationPostURL() {
		return baseAPIURL() + "reservations";
	}

	public static String getRegisterDiscountsPostURL(String uuid) {
		return baseAPIURL() + "operations/" + uuid + "/discounts";
	}

	public static String getRetrieveUserInfoServiceGetURL() {
		return baseAPIURL() + "currentUser/info";
	}

	public static String getRetrieveMyShowsServiceGetURL() {
		return baseAPIURL() + "currentUser/operations";
	}

	public static String getRetrieveUserShowInfoServiceGetURL(String userShowId) {
		return baseAPIURL() + "currentUser/operations/" + userShowId;
	}

	public static String getCancelUserShowServiceDeleteURL(String id) {
		return baseAPIURL() + "reservations/" + id;
	}

	public static String getConfirmReservationServicePostURL(
			String reservationId) {
		return baseAPIURL() + "reservations/" + reservationId + "/confirm";
	}

	public static String getRetrieveCreditCardsServiceGetURL() {
		return baseAPIURL() + "creditCards";
	}
}
