package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.PostedTicket;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class RegisterReservationService extends PostService implements
		IRegisterReservationService {

	private PostedTicket postObject;
	private IRegisterReservationServiceDelegate delegate;

	public RegisterReservationService() {
		performingRequest = false;
	}

	@Override
	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			PostedTicket purchaseTicket) {
		if (true == performingRequest)
			return;
		if (null == delegate || null == purchaseTicket)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = purchaseTicket;
		connectionSuccess = false;
		execute(APIService.getRegisterReservationPostURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (201 != statusLine.getStatusCode()) {
				delegate.reserveTicketFinishWithError(this,
						statusLine.getStatusCode());
			} else {
				delegate.reserveTicketFinish(this, result);
			}
		} else {
			delegate.reserveTicketFinishWithError(this, 0);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postObject, PostedTicket.class);
		return jsonString;
	}
}
