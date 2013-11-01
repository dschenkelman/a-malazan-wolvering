package phoneticket.android.services.post.impl;

import phoneticket.android.model.PostedTicket;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;

public class RegisterReservationServiceProxy implements
		IRegisterReservationService {

	@Override
	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			PostedTicket ticket) {
		(new RegisterReservationService()).reserveTicket(delegate, ticket);
	}

}
