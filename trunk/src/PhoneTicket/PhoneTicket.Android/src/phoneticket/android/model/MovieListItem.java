package phoneticket.android.model;

public class MovieListItem implements IMovieListItem {

	private int id;
	private String title;
	private String imageUrl;

	public MovieListItem(int id, String title, String imageURL) {
		this.id = id;
		this.title = title;
		this.imageUrl = imageURL;
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
	public String getImageURL() {
		return imageUrl;
	}
}
