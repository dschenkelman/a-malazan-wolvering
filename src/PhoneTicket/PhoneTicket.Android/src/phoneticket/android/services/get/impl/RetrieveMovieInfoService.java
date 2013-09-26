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
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveMovieInfoFinishWithError(this, 1);
		} else {
			movie = new Gson().fromJson(result, Movie.class);
			delegate.retrieveMovieInfoFinish(this, movie);
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
