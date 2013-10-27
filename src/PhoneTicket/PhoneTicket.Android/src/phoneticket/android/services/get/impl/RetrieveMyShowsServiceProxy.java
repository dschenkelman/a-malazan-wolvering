package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveMyShowsService;

public class RetrieveMyShowsServiceProxy implements IRetrieveMyShowsService {

	@Override
	public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
		(new MockRetrieveMyShowsService()).retrieveMyShows(delegate);
	}

}
