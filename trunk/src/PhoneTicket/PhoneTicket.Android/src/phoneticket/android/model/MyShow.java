package phoneticket.android.model;

public class MyShow implements IMyShow{
	
	private int id;
	private boolean isBought;
	private String showTime;
	private String movieName;
	private String complexAddress;

	public MyShow(int id, boolean isBought, String movieName,
			String showTime, String complexAddress) {
		this.id = id;
		this.isBought = isBought;
		this.showTime = showTime;
		this.movieName = movieName;
		this.complexAddress = complexAddress;
	}

	@Override
	public boolean isBought() {
		return isBought;
	}

	@Override
	public String getShowDateAndTime() {
		return showTime;
	}

	@Override
	public String getMovieTitle() {
		return movieName;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getComplexAddress() {
		return complexAddress;
	}

}
