package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;

public class RetrieveMovieInfoServiceProxy implements IRetrieveMovieInfoService {

	// Se Hace de esta manera porque un AsynTask en Android solo puede ser
	// ejecutada una vez
	@Override
	public void retrieveMovieInfo(IRetrieveMovieInfoServiceDelegate delegate,
			int movieId) {
		(new RetrieveMovieInfoServiceProxy()).retrieveMovieInfo(delegate, movieId);
	}

}
