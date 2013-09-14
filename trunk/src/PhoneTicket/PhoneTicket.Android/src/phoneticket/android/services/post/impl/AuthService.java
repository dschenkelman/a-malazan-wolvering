package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;
import phoneticket.android.utils.UserManager;

public class AuthService extends PostService implements IAuthService {

	private LoginUser postBodyObject;
	private IAuthServiceDelegate delegate;

	public AuthService() {
		performingRequest = false;
	}

	@Override
	public void authUser(IAuthServiceDelegate delegate, LoginUser user) {
		if (performingRequest || null == user || null == delegate)
			return;
		postBodyObject = user;
		performingRequest = true;
		this.delegate = delegate;
		execute(APIService.getAuthServicePostURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (connectionSuccess) {
			if (statusLine.getStatusCode() >= 400) {
				delegate.authServiceDelegateFinishWithError(this,
						statusLine.getStatusCode());
			} else {
				int id = Integer.parseInt(result);
				UserManager.getInstance().loginUserWithId(id,true);
				delegate.authServiceDelegateFinish(this, postBodyObject);
			}
			performingRequest = false;
			delegate = null;
		} else {
			delegate.authServiceDelegateFinishWithError(this, 0);
		}
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postBodyObject, LoginUser.class);
		return jsonString;
	}
}
