package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveMovieFunctionsService;

public class RetrieveMovieFunctionsServiceProxy implements
		IRetrieveMovieFunctionsService {

	@Override
	public void retrieveMovieFunctions(
			IRetrieveMovieFunctionsServiceDelegate delegate, int movieId) {
		(new MockRetrieveMovieFunctionsService()).retrieveMovieFunctions(
				delegate, movieId);
	}
}
