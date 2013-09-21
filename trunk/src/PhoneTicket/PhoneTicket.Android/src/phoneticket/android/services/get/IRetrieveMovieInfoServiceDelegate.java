package phoneticket.android.services.get;

import phoneticket.android.model.IMovie;

public interface IRetrieveMovieInfoServiceDelegate {
	
	void retrieveMovieInfoFinish(IRetrieveMovieInfoService service,
			IMovie movie);

	void retrieveMovieInfoFinishWithError(IRetrieveMovieInfoService service,
			Integer errorMessage);
}
