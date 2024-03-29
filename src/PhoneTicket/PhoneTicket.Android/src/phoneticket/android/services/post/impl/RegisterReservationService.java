package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.ReserveTicket;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class RegisterReservationService extends PostService implements
		IRegisterReservationService {

	private ReserveTicket postObject;
	private IRegisterReservationServiceDelegate delegate;

	public RegisterReservationService() {
		performingRequest = false;
	}

	@Override
	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			ReserveTicket ticket) {
		if (true == performingRequest)
			return;
		if (null == delegate || null == ticket)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = ticket;
		connectionSuccess = false;
		execute(APIService.getRegisterReservationPostURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (200 != statusLine.getStatusCode()) {
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
		String jsonString = new Gson().toJson(postObject, ReserveTicket.class);
		return jsonString;
	}
}
