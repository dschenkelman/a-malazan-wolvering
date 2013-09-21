package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.MovieListItem;

public interface IRetrieveMovieListServiceDelegate {

	void retrieveMovieListServiceFinish(IRetrieveMovieListService service,
			Collection<MovieListItem> movieList);

	void retrieveMovieListFinishWithError(IRetrieveMovieListService service,
			Integer errorMessage);
}
