package phoneticket.android.services.delete.impl;

import org.apache.http.client.ClientProtocolException;

import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.delete.DeleteService;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.delete.ICancelUserShowServiceDelegate;
import phoneticket.android.utils.APIService;

public class CancelUserShowService extends DeleteService implements
		ICancelUserShowService {

	private ICancelUserShowServiceDelegate delegate;

	public CancelUserShowService() {
		performingRequest = false;
	}

	@Override
	public void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow) {
		if (performingRequest || null == delegate || null == userShow) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getCancelUserShowServiceDeleteURL(userShow.getId()));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (null == result) {
			delegate.cancelUserShowFinishedWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			if (200 == statusLine.getStatusCode()) {
				delegate.cancelUserShowFinished(this);
			} else {
				delegate.cancelUserShowFinishedWithError(this, 2);
			}
		}
		delegate = null;
		performingRequest = false;
	}

	@Override
	protected void handleStatusCodeNotOk(Exception e, int statusCode) {
		super.handleStatusCodeNotOk(e, statusCode);
		performingRequest = false;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		super.handleClientProtocolException(e);
		performingRequest = false;
	}
}
