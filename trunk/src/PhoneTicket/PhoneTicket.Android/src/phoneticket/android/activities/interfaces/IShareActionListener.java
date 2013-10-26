package phoneticket.android.activities.interfaces;

public interface IShareActionListener {

	void shareOnTwitter(String twitterMessage);

	void shareMovieOnFacebook(String url);

	void shareCinemaOnFacebook(double latitude, double longitude,
			String address, String cinemaName);
}
