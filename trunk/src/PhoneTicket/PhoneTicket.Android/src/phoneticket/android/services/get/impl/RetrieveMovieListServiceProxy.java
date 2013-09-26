package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;

public class RetrieveMovieListServiceProxy implements IRetrieveMovieListService {

	@Override
	public void retrieveMovieList(IRetrieveMovieListServiceDelegate delegate) {
		(new RetrieveMovieListService()).retrieveMovieList(delegate);
	}
}