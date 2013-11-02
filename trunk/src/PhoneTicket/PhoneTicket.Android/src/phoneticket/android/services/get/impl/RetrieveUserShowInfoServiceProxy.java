package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;

public class RetrieveUserShowInfoServiceProxy implements
		IRetrieveUserShowInfoService {

	@Override
	public void retrieveUserShowInfo(
			IRetrieveUserShowInfoServiceDelegate delegate, String userShowId) {
		(new RetrieveUserShowInfoService()).retrieveUserShowInfo(delegate,
				userShowId);
	}
}
