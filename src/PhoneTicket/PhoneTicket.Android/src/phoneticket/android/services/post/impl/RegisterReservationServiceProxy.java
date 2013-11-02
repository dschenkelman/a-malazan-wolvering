package phoneticket.android.services.post.impl;

import phoneticket.android.model.ReserveTicket;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;

public class RegisterReservationServiceProxy implements
		IRegisterReservationService {

	@Override
	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			ReserveTicket ticket) {
		(new RegisterReservationService()).reserveTicket(delegate, ticket);
	}

}
