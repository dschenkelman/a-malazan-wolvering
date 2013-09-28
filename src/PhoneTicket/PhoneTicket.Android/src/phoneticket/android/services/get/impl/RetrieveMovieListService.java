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
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveMovieListServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<IMovieListItem> movieList = new ArrayList<IMovieListItem>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveMovieListFinishWithError(this, statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMovieListItems = new JSONArray(result);
				for (int i = 0; i < jsonMovieListItems.length(); i++) {
					JSONObject jsonMovieListItem = jsonMovieListItems
							.getJSONObject(i);
					IMovieListItem moviewList = new Gson().fromJson(
							jsonMovieListItem.toString(), MovieListItem.class);
					if (null != moviewList) {
						movieList.add(moviewList);
					}
				}
				delegate.retrieveMovieListFinish(this, movieList);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveMovieListFinishWithError(this, 2);
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
