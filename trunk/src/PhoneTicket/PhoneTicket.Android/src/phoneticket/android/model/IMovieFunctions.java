package phoneticket.android.model;

import java.util.Collection;

public interface IMovieFunctions {

	int getMovieId();

	int getCinemaId();

	String getCinemaName();

	String getCinemaAddress();

	Collection<IFunction> getFunctions();
}
