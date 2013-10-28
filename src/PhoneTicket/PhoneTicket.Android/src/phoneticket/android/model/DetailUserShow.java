package phoneticket.android.model;

public class DetailUserShow implements IDetailUserShow {

	private int id;
	private boolean isBought;
	private String movieName;
	private String showTime;
	private String complexAddress;
	private int ticketsCount;

	public DetailUserShow(int id, boolean isBought, String movieName,
			String showTime, String complexAddress, int ticketsCount) {

		this.id = id;
		this.isBought = isBought;
		this.movieName = movieName;
		this.showTime = showTime;
		this.complexAddress = complexAddress;
		this.ticketsCount = ticketsCount;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean isBought() {
		return isBought;
	}

	@Override
	public String getMovieName() {
		return movieName;
	}

	@Override
	public String getShowTime() {
		return showTime;
	}

	@Override
	public String getComplexAddress() {
		return complexAddress;
	}

	@Override
	public int getTicketsCount() {
		return ticketsCount;
	}

}
