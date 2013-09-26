package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;

public class RetrieveMovieInfoServiceProxy implements IRetrieveMovieInfoService {

	@Override
	public void retrieveMovieInfo(IRetrieveMovieInfoServiceDelegate delegate,
			int movieId) {
		(new RetrieveMovieInfoService()).retrieveMovieInfo(delegate, movieId);
	}

}
