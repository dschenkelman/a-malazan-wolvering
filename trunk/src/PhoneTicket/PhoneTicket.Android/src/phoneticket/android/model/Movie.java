package phoneticket.android.model;

public class Movie implements IMovie {

	private int id;
	private String title;
	private String synopsis;
	private String imageUrl;
	private String rating;
	private int durationInMinutes;
	private String genre;
	private String trailerUrl;

	public Movie(int id, String title, String synopsis, String imageURL,
			String clasification, int durationInMinutes, String gendre,
			String youtubeVideoURL) {
		this.id = id;
		this.title = title;
		this.synopsis = synopsis;
		this.imageUrl = imageURL;
		this.rating = clasification;
		this.durationInMinutes = durationInMinutes;
		this.genre = gendre;
		this.trailerUrl = youtubeVideoURL;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getSynopsis() {
		return synopsis;
	}

	@Override
	public String getImageURL() {
		return imageUrl;
	}

	@Override
	public String getClasification() {
		return rating;
	}

	@Override
	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	@Override
	public String getGenre() {
		return genre;
	}

	@Override
	public String getTrailerUrl() {
		return trailerUrl;
	}
}
