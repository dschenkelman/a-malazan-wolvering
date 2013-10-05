package phoneticket.android.model;

import java.util.Collection;

public interface IMovieFunctions {

	int getMovieId();

	int getCinemaId();

	String getCinemaName();

	Collection<IFunction> getFunctions();
}
