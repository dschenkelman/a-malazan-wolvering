package phoneticket.android.services.get.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.DetailUserShow;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveUserShowInfoService extends GetService implements
		IRetrieveUserShowInfoService {

	private IRetrieveUserShowInfoServiceDelegate delegate;

	public RetrieveUserShowInfoService() {
		performingRequest = false;
	}

	@Override
	public void retrieveUserShowInfo(
			IRetrieveUserShowInfoServiceDelegate delegate, String userShowId) {
		if (true == performingRequest || null == delegate || null == userShowId) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveUserShowInfoServiceGetURL(userShowId));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveUserShowInfoFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONObject jsonUser = new JSONObject(result);
				DetailUserShow detail = new Gson()
						.fromJson(jsonUser.toString(), DetailUserShow.class);
				delegate.retrieveUserShowInfoFinish(this, detail);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveUserShowInfoFinishWithError(this, 2);
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
