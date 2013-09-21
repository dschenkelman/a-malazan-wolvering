package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.IMovieListItem;
import phoneticket.android.model.MovieListItem;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveMovieListService extends GetService implements
		IRetrieveMovieListService {

	private IRetrieveMovieListServiceDelegate delegate;

	public RetrieveMovieListService() {
		performingRequest = false;
	}

	@Override
	public void retrieveMovieList(IRetrieveMovieListServiceDelegate delegate) {
		if (true == performingRequest && null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveMovieListServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<IMovieListItem> toilets = new ArrayList<IMovieListItem>();
		if (null != result) {
			try {
				JSONArray jsonToilets = new JSONArray(result);
				for (int i = 0; i < jsonToilets.length(); i++) {
					JSONObject jsonToilet = jsonToilets.getJSONObject(i);
					IMovieListItem toilet = new Gson().fromJson(
							jsonToilet.toString(), MovieListItem.class);
					if (null != toilet) {
						toilets.add(toilet);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			delegate.retrieveMovieListFinish(this, toilets);
		} else {
			delegate.retrieveMovieListFinishWithError(this, 1);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		delegate.retrieveMovieListFinishWithError(this, 2);
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		delegate.retrieveMovieListFinishWithError(this, 3);
		performingRequest = false;
		delegate = null;
	}
}
