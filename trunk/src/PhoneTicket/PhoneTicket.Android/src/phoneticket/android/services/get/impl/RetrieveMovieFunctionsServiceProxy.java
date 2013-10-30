package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;

public class RetrieveMovieFunctionsServiceProxy implements
		IRetrieveMovieFunctionsService {

	@Override
	public void retrieveMovieFunctions(
			IRetrieveMovieFunctionsServiceDelegate delegate, int movieId) {
		(new RetrieveMovieFunctionsService()).retrieveMovieFunctions(delegate,
				movieId);
	}
}
