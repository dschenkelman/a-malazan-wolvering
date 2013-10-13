package phoneticket.android.services.get.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;

import phoneticket.android.model.Cinema;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaInfoServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveCinemaInfoService extends GetService implements
		IRetrieveCinemaInfoService {

	private IRetrieveCinemaInfoServiceDelegate delegate;

	public RetrieveCinemaInfoService() {
		performingRequest = false;
	}

	@Override
	public void retrieveCinemaInfo(IRetrieveCinemaInfoServiceDelegate delegate,
			int cinemaId) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveCinemaInfoServiceGetURL(cinemaId));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveCinemaInfoFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			ICinema cinema = new Gson().fromJson(result, Cinema.class);
			if (null != cinema) {
				delegate.retrieveCinemaInfoFinish(this, cinema);
			} else {
				delegate.retrieveCinemaInfoFinishWithError(this, 2);
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
