package phoneticket.android.model;

public class MyShow implements IMyShow{
	
	private String id;
	private boolean isBought;
	private String showDateAndTime;
	private String movieTitle;
	private String complexAddress;

	public MyShow(String id, boolean isBought, String movieName,
			String showTime, String complexAddress) {
		this.id = id;
		this.isBought = isBought;
		this.showDateAndTime = showTime;
		this.movieTitle = movieName;
		this.complexAddress = complexAddress;
	}

	@Override
	public boolean isBought() {
		return isBought;
	}

	@Override
	public String getShowDateAndTime() {
		return showDateAndTime;
	}

	@Override
	public String getMovieTitle() {
		return movieTitle;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getComplexAddress() {
		return complexAddress;
	}

}
