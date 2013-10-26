package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveCinemaListService;

public class RetrieveCinemaListServiceProxy implements
		IRetrieveCinemaListService {

	@Override
	public void retrieveCinemaList(IRetrieveCinemaListServiceDelegate delegate) {
		(new MockRetrieveCinemaListService()).retrieveCinemaList(delegate);
	}

}
