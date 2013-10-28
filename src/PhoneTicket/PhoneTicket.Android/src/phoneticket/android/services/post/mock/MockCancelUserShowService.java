package phoneticket.android.services.post.mock;

import android.os.AsyncTask;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.post.ICancelUserShowService;
import phoneticket.android.services.post.ICancelUserShowServiceDelegate;

public class MockCancelUserShowService extends
		AsyncTask<String, String, String> implements ICancelUserShowService {
	
	private ICancelUserShowServiceDelegate delegate;
	private boolean fakeTimeout;

	@Override
	public void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow) {
		fakeTimeout = true;
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
			delegate.cancelUserShowFinishedWithError(this, -1);
		} else {
			delegate.cancelUserShowFinished(this);
		}
	}
}
