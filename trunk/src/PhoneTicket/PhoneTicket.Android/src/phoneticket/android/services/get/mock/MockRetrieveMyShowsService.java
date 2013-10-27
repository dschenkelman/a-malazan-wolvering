package phoneticket.android.services.get.mock;

import java.util.ArrayList;
import java.util.Collection;

import android.os.AsyncTask;
import phoneticket.android.model.IMyShow;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;

public class MockRetrieveMyShowsService extends
		AsyncTask<String, String, String> implements IRetrieveMyShowsService {

	private IRetrieveMyShowsServiceDelegate delegate;
	private boolean fakeTimeout;
	
	@Override
	public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
		fakeTimeout = false;
		this.delegate = delegate;
		execute("");
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Thread.sleep(fakeTimeout ? 1500 : 500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		if (fakeTimeout) {
			delegate.retrieveMyShowsServiceFinishedWithError(this, -1);
		} else {
			delegate.retrieveMyShowsServiceFinished(this, createMocks());
		}
	}

	private Collection<IMyShow> createMocks() {
		Collection<IMyShow> myShows = new ArrayList<IMyShow>();
		return myShows;
	}
}
