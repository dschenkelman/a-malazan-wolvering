package phoneticket.android.services.post;

import phoneticket.android.model.PostedTicket;

public interface IRegisterReservationService {

	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			PostedTicket ticket);

}
