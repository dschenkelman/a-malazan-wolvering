package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.Cinema;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveCinemaListService extends GetService implements
		IRetrieveCinemaListService {

	private IRetrieveCinemaListServiceDelegate delegate;

	public RetrieveCinemaListService() {
		performingRequest = false;
	}

	@Override
	public void retrieveCinemaList(IRetrieveCinemaListServiceDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveCinemaListServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<ICinema> cinemaList = new ArrayList<ICinema>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveCinemaListFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMovieListItems = new JSONArray(result);
				for (int i = 0; i < jsonMovieListItems.length(); i++) {
					JSONObject jsonMovieListItem = jsonMovieListItems
							.getJSONObject(i);
					ICinema cinema = new Gson().fromJson(
							jsonMovieListItem.toString(), Cinema.class);
					if (null != cinema) {
						cinemaList.add(cinema);
					}
				}
				delegate.retrieveCinemaListFinish(this, cinemaList);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveCinemaListFinishWithError(this, 2);
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
