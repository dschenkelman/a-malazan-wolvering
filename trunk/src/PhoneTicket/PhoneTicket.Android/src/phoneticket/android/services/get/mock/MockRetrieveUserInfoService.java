package phoneticket.android.services.get.mock;

import phoneticket.android.model.IUser;
import phoneticket.android.model.User;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.utils.UserManager;

public class MockRetrieveUserInfoService implements IRetrieveUserInfoService {

	@Override
	public void retrieveUserInfo(IRetrieveUserInfoServiceDelegate delegate,
			int userId) {
		UserManager.getInstance().updateUserData("Matias", "Servetto", "04/07/2013", "+5491137574067");
		delegate.retrieveUserInfoFinish(this);
	}

}
