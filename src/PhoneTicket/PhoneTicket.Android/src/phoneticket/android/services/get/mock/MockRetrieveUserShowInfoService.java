package phoneticket.android.services.get.mock;

import android.os.AsyncTask;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;

public class MockRetrieveUserShowInfoService extends
		AsyncTask<String, String, String> implements
		IRetrieveUserShowInfoService {

	private IRetrieveUserShowInfoServiceDelegate delegate;
	private boolean fakeTimeout;
	private int id;

	@Override
	public void retrieveUserShowInfo(
			IRetrieveUserShowInfoServiceDelegate delegate, int userShowId) {
		fakeTimeout = false;
		this.delegate = delegate;
		this.id = userShowId;
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
			delegate.retrieveUserShowInfoFinishWithError(this, -1);
		} else {
			delegate.retrieveUserShowInfoFinish(this, createMocks());
		}
	}

	private IDetailUserShow createMocks() {
		IDetailUserShow detail = new IDetailUserShow() {
			@Override
			public int getId() {
				return id;
			}

			@Override
			public boolean isBought() {
				return 0 == (id % 2);
			}

			@Override
			public String getMovieName() {
				return "Aviones";
			}

			@Override
			public String getShowTime() {
				return "19/02 16:00 Hs";
			}

			@Override
			public String getComplexAddress() {
				return "Las Heras 999";
			}

			@Override
			public int getTicketsCount() {
				return 4;
			}
		};
		return detail;
	}
}
