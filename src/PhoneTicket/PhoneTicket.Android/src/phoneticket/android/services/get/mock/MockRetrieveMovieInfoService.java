package phoneticket.android.services.get.mock;

import android.os.AsyncTask;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.Movie;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;

public class MockRetrieveMovieInfoService extends
		AsyncTask<String, String, String> implements IRetrieveMovieInfoService {

	private int movieId;
	private IRetrieveMovieInfoServiceDelegate delegate;

	@Override
	public void retrieveMovieInfo(IRetrieveMovieInfoServiceDelegate delegate,
			int movieId) {
		this.movieId = movieId;
		this.delegate = delegate;
		execute("");
	}

	private IMovie createMockMovie(int movieId) {
		return new Movie(
				movieId,
				"El Conjuro",
				"El conjuro se basa en los sucesos sobrenaturales que ocurrieron en la casa de Rhode Island de la familia Perron y que investigaron Ed y Lorraine Warren, expertos en actividades paranormales",
				"", "P16", 112, "Terror",
				"http://www.youtube.com/watch?v=OJgDCNyfWfQ");
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (0 < movieId)
			delegate.retrieveMovieInfoFinish(this, createMockMovie(movieId));
		else
			delegate.retrieveMovieInfoFinishWithError(this, -1);
	}
}
