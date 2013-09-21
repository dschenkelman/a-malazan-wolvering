package phoneticket.android.model;

public class Movie implements IMovie {

	private int id;
	private String title;
	private String synopsis;
	private String imageURL;
	private String clasification;
	private int durationInMinutes;
	private String gendre;
	private String youtubeVideoURL;

	public Movie(int id, String title, String synopsis, String imageURL,
			String clasification, int durationInMinutes, String gendre,
			String youtubeVideoURL) {
		this.id = id;
		this.title = title;
		this.synopsis = synopsis;
		this.imageURL = imageURL;
		this.clasification = clasification;
		this.durationInMinutes = durationInMinutes;
		this.gendre = gendre;
		this.youtubeVideoURL = youtubeVideoURL;
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
		return imageURL;
	}

	@Override
	public String getClasification() {
		return clasification;
	}

	@Override
	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	@Override
	public String getGendre() {
		return gendre;
	}

	@Override
	public String getYoutubeVideoURL() {
		return youtubeVideoURL;
	}
}
