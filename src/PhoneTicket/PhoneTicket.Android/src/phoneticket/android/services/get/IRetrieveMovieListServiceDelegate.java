package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.IMovieListItem;

public interface IRetrieveMovieListServiceDelegate {

	void retrieveMovieListFinish(IRetrieveMovieListService service,
			Collection<IMovieListItem> movieList);

	void retrieveMovieListFinishWithError(IRetrieveMovieListService service,
			Integer errorMessage);
}
