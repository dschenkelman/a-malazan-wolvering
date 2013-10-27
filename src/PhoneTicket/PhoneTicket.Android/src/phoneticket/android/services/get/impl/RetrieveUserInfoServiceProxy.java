package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveUserInfoService;

public class RetrieveUserInfoServiceProxy implements IRetrieveUserInfoService {

	@Override
	public void retrieveUserInfo(IRetrieveUserInfoServiceDelegate delegate,
			int userId) {
		(new MockRetrieveUserInfoService()).retrieveUserInfo(delegate, userId);
	}
}
