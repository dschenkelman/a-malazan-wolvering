package phoneticket.android.services.get.mock;

import android.os.AsyncTask;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.utils.UserManager;

public class MockRetrieveUserInfoService extends
		AsyncTask<String, String, String> implements IRetrieveUserInfoService {

	private IRetrieveUserInfoServiceDelegate delegate;
	private boolean fakeTimeout;
	
	@Override
	public void retrieveUserInfo(IRetrieveUserInfoServiceDelegate delegate,
			int userId) {
		fakeTimeout = false;
		this.delegate = delegate;
		execute("");
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Thread.sleep(fakeTimeout ? 1500: 500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		if (fakeTimeout) {
			delegate.retrieveUserInfoFinishWithError(this, -1);
		} else {
			UserManager.getInstance().updateUserData("Matias", "Servetto",
					"04/07/2013", "+5491137574067");
			delegate.retrieveUserInfoFinish(this);
		}
	}
}
