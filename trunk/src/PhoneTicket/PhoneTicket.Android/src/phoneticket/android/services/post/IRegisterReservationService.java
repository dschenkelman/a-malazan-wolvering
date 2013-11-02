package phoneticket.android.services.post;

import phoneticket.android.model.ReserveTicket;

public interface IRegisterReservationService {

	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			ReserveTicket ticket);

}
