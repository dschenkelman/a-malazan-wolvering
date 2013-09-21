package phoneticket.android.services.get.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

import phoneticket.android.model.IMovie;
import phoneticket.android.model.Movie;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveMovieInfoService extends GetService implements
		IRetrieveMovieInfoService {

	private IRetrieveMovieInfoServiceDelegate delegate;

	public RetrieveMovieInfoService() {
		performingRequest = false;
	}

	@Override
	public void retrieveMovieInfo(IRetrieveMovieInfoServiceDelegate delegate,
			int movieId) {
		if (true == performingRequest || null == delegate || 0 > movieId) {
			return;
		}
		performingRequest = true;
		this.delegate = delegate;
		execute(APIService.getRetrieveMovieGetURL(movieId + ""));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		IMovie movie = null;
		if (null != result) {
			movie = new Gson().fromJson(result, Movie.class);
		} else {
			delegate.retrieveMovieInfoFinishWithError(this, 1);
		}
		delegate.retrieveMovieInfoFinish(this, movie);
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		delegate.retrieveMovieInfoFinishWithError(this, 2);
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		delegate.retrieveMovieInfoFinishWithError(this, 3);
		performingRequest = false;
		delegate = null;
	}
}
