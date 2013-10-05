package phoneticket.android.model;

import java.util.Collection;

public class MovieFunctions implements IMovieFunctions {

	private int movieId;
	private int cinemaId;
	private String cinemaName;
	Collection<IFunction> functions;
	
	public MovieFunctions(int movieId, int cinemaId, String cinemaName, Collection<IFunction> functions) {
		this.movieId = movieId;
		this.cinemaId = cinemaId;
		this.cinemaName = cinemaName;
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
		return functions;
	}
}
