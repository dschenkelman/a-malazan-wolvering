package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;

public class RetrieveMyShowsServiceProxy implements IRetrieveMyShowsService {

	@Override
	public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
		(new RetrieveMyShowsService()).retrieveMyShows(delegate);
	}

}
