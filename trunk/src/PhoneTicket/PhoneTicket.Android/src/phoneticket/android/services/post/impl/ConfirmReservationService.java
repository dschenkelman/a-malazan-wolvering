package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.CreditCardData;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.services.post.IConfirmReservationServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class ConfirmReservationService extends PostService implements
		IConfirmReservationService {

	private CreditCardData postObject;
	private IConfirmReservationServiceDelegate delegate;

	public ConfirmReservationService() {
		performingRequest = false;
	}

	@Override
	public void confirmReservation(IConfirmReservationServiceDelegate delegate,
			CreditCardData data, String reservationId) {
		if (true == performingRequest || null == delegate || null == data)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = data;
		connectionSuccess = false;
		execute(APIService.getConfirmReservationServicePostURL(reservationId));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (200 != statusLine.getStatusCode()) {
				delegate.confirmReservationFinishedWithError(this,
						statusLine.getStatusCode());
			} else {
				delegate.confirmReservationFinished(this);
			}
		} else {
			delegate.confirmReservationFinishedWithError(this, 0);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postObject, CreditCardData.class);
		return jsonString;
	}
}
