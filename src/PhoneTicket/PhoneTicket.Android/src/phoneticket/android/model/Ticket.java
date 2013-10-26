package phoneticket.android.model;

import java.io.Serializable;

public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cinemaId;
	private String cinemaName;
	private String cinemaAddress;

	private int movieId;
	private String movieTitle;

	private int functionId;
	private String functionDay;
	private String functionTime;

	private int roomId;

	public Ticket(int cinemaId, String cinemaName, String cinemaAddress,
			int movieId, String movieTitle, int functionId, String functionDay,
			String functionTime, int roomId) {
		this.cinemaId = cinemaId;
		this.cinemaName = cinemaName;
		this.cinemaAddress = cinemaAddress;
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.functionId = functionId;
		this.functionDay = functionDay;
		this.functionTime = functionTime;
		this.roomId = roomId;
	}

	public int getCinemaId() {
		return cinemaId;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public String getCinemaAddress() {
		return cinemaAddress;
	}

	public int getMovieId() {
		return movieId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public int getFunctionId() {
		return functionId;
	}

	public String getFunctionDay() {
		return functionDay;
	}

	public String getFunctionTime() {
		return functionTime;
	}

	public int getRoomId() {
		return this.roomId;
	}

}
