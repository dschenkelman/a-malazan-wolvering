package phoneticket.android.services.get.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.User;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.utils.APIService;
import phoneticket.android.utils.UserManager;

public class RetrieveUserInfoService extends GetService implements
		IRetrieveUserInfoService {

	private IRetrieveUserInfoServiceDelegate delegate;

	public RetrieveUserInfoService() {
		performingRequest = false;
	}

	@Override
	public void retrieveUserInfo(IRetrieveUserInfoServiceDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveUserInfoServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveUserInfoFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONObject jsonUser = new JSONObject(result);
				User user = new Gson()
						.fromJson(jsonUser.toString(), User.class);
				UserManager.getInstance().updateUserData(user.getFirstName(),
						user.getLastName(), user.getBirthday(),
						user.getCellPhone());
				delegate.retrieveUserInfoFinish(this);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveUserInfoFinishWithError(this, 2);
			}
		}
		delegate = null;
		performingRequest = false;
	}

	@Override
	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		super.handleStatusCodeNotOk(e, statusCode);
		isStatusOk = false;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		super.handleClientProtocolException(e);
		hasCLientProtocolRecieveException = true;
	}

}
