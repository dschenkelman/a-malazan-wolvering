package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveUserShowInfoService;

public class RetrieveUserShowInfoServiceProxy implements
		IRetrieveUserShowInfoService {

	@Override
	public void retrieveUserShowInfo(
			IRetrieveUserShowInfoServiceDelegate delegate, int userShowId) {
		(new MockRetrieveUserShowInfoService()).retrieveUserShowInfo(delegate,
				userShowId);
	}
}
