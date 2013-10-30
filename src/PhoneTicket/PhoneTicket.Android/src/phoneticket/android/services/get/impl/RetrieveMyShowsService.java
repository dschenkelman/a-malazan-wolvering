package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.IMyShow;
import phoneticket.android.model.MyShow;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveMyShowsService extends GetService implements
		IRetrieveMyShowsService {

	private IRetrieveMyShowsServiceDelegate delegate;

	public RetrieveMyShowsService() {
		performingRequest = false;
	}

	@Override
	public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveMyShowsServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<IMyShow> myShows = new ArrayList<IMyShow>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveMyShowsServiceFinishedWithError(this, null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMyShows = new JSONArray(result);
				for (int i = 0; i < jsonMyShows.length(); i++) {
					JSONObject jsonMyShow = jsonMyShows
							.getJSONObject(i);
					IMyShow myShow = new Gson().fromJson(
							jsonMyShow.toString(), MyShow.class);
					if (null != myShow) {
						myShows.add(myShow);
					}
				}
				delegate.retrieveMyShowsServiceFinished(this, myShows);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveMyShowsServiceFinishedWithError(this, 2);
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
