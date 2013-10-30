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
			Thread.sleep(fakeTimeout ? 1500 : 750);
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
		myShows.add(new IMyShow() {
			@Override
			public int getId() {
				return 1;
			}
			@Override
			public boolean isBought() {
				return false;
			}
			@Override
			public String getMovieTitle() {
				return "Aviones";
			}
			@Override
			public String getShowDateAndTime() {
				return "19/02 16:00 Hs";
			}
			@Override
			public String getComplexAddress() {
				return "Las Heras 999";
			}
		});
		myShows.add(new IMyShow() {
			@Override
			public int getId() {
				return 2;
			}
			@Override
			public boolean isBought() {
				return true;
			}
			@Override
			public String getMovieTitle() {
				return "El Conjuro";
			}
			@Override
			public String getShowDateAndTime() {
				return "21/02 19:00 Hs";
			}
			@Override
			public String getComplexAddress() {
				return "Las Heras 999";
			}
		});
		myShows.add(new IMyShow() {
			@Override
			public int getId() {
				return 3;
			}
			@Override
			public boolean isBought() {
				return false;
			}
			@Override
			public String getMovieTitle() {
				return "Dragon Ball";
			}
			@Override
			public String getShowDateAndTime() {
				return "23/02 5:30 Hs";
			}
			@Override
			public String getComplexAddress() {
				return "Av. Belgrano 456";
			}
		});
		return myShows;
	}
}
