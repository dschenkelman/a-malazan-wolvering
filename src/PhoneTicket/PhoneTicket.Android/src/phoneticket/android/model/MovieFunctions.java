package phoneticket.android.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieFunctions implements IMovieFunctions {

	private int movieId;
	private int cinemaId;
	private String cinemaName;
	Collection<Function> functions;
	private String cinemaAddress;

	public MovieFunctions(int movieId, int cinemaId, String cinemaName,
			String cinemaAddress, Collection<Function> functions) {
		this.movieId = movieId;
		this.cinemaId = cinemaId;
		this.cinemaName = cinemaName;
		this.cinemaAddress = cinemaAddress;
		this.functions = functions;
	}

	@Override
	public int getMovieId() {
		return movieId;
	}

	@Override
	public int getCinemaId() {
		return cinemaId;
	}

	@Override
	public String getCinemaName() {
		return cinemaName;
	}

	@Override
	public Collection<IFunction> getFunctions() {
		List<IFunction> ifunctions = new ArrayList<IFunction>();
		ifunctions.addAll(functions);
		return ifunctions;
	}

	@Override
	public String getCinemaAddress() {
		return this.cinemaAddress;
	}
}
