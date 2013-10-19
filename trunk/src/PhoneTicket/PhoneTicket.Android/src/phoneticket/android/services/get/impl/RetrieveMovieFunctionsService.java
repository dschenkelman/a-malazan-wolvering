package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.IMovieFunctions;
import phoneticket.android.model.MovieFunctions;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveMovieFunctionsService extends GetService implements
		IRetrieveMovieFunctionsService {

	private IRetrieveMovieFunctionsServiceDelegate delegate;

	public RetrieveMovieFunctionsService() {
		performingRequest = false;
	}

	@Override
	public void retrieveMovieFunctions(
			IRetrieveMovieFunctionsServiceDelegate delegate, int movieId) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveMovieFunctionsGetURL(movieId + ""));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<IMovieFunctions> movieFunctions = new ArrayList<IMovieFunctions>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveMovieFunctionsFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMovieListItems = new JSONArray(result);
				for (int i = 0; i < jsonMovieListItems.length(); i++) {
					JSONObject jsonMovieListItem = jsonMovieListItems
							.getJSONObject(i);
					IMovieFunctions cinema = new Gson().fromJson(
							jsonMovieListItem.toString(), MovieFunctions.class);
					if (null != cinema) {
						movieFunctions.add(cinema);
					}
				}
				delegate.retrieveMovieFunctionsFinish(this, movieFunctions);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveMovieFunctionsFinishWithError(this, 2);
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
