package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class RegisterUserService extends PostService implements
		IRegisterUserService {

	private User postObject;
	private IRegisterUserServiceDelegate delegate;

	public RegisterUserService() {
		performingRequest = false;
	}

	@Override
	public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
		if (true == performingRequest)
			return;
		if (null == delegate || null == user)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = user;
		connectionSuccess = false;
		execute(APIService.getRegisterUserPostURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (201 != statusLine.getStatusCode()) {
				delegate.registerUserFinishWithError(this,
						statusLine.getStatusCode());
			} else {
				delegate.registerUserFinish(this, postObject);
			}
		} else {
			delegate.registerUserFinishWithError(this, 0);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postObject, User.class);
		return jsonString;
	}
}
