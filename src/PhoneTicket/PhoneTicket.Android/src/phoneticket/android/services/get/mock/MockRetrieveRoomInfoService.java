package phoneticket.android.services.get.mock;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import android.os.AsyncTask;

import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;

public class MockRetrieveRoomInfoService extends
		AsyncTask<String, String, String> implements IRetrieveRoomInfoService {

	private IRetrieveRoomInfoServiceDelegate delegate;

	@Override
	public void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate,
			int roomId) {
		this.delegate = delegate;
		execute("");
	}

	private Collection<Collection<Integer>> getRoom() {
		Collection<Collection<Integer>> movieList = new LinkedList<Collection<Integer>>();
		Random r = new Random();
		for (int i = 0; i < 17; i++) {
			Collection<Integer> row = new LinkedList<Integer>();
			for (int j = 0; j < 5; j++) {
				row.add(0);
			}
			for (int j = 5; j < 17; j++) {
				Integer random = r.nextInt(3);
				row.add(random);
			}
			for (int j = 17; j < 22; j++) {
				row.add(0);
			}
			movieList.add(row);
		}
		return movieList;

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
		delegate.retrieveRoomInfoFinish(this, this.getRoom());
		// delegate.retrieveRoomInfoFinishWithError(this, 5);
	}
}
