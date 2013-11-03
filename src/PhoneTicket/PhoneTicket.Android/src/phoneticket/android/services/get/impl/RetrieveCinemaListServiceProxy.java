package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;

public class RetrieveCinemaListServiceProxy implements
		IRetrieveCinemaListService {

	@Override
	public void retrieveCinemaList(IRetrieveCinemaListServiceDelegate delegate) {
		(new RetrieveCinemaListService()).retrieveCinemaList(delegate);
	}

}
