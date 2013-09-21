package phoneticket.android.services.get;

import android.graphics.Movie;

public interface IRetrieveMovieInfoServiceDelegate {
	
	void retrieveMovieInfoServiceFinish(IRetrieveMovieInfoService service,
			Movie movie);

	void retrieveMovieInfoFinishWithError(IRetrieveMovieInfoService service,
			Integer errorMessage);
}
