package phoneticket.android.services.post.impl.mock;

import phoneticket.android.model.ReserveTicket;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;

public class MockRegisterReservationService implements
		IRegisterReservationService {
	private String uuid;

	@Override
	public void reserveTicket(IRegisterReservationServiceDelegate delegate,
			ReserveTicket ticket) {
		uuid = "1234";
		delegate.reserveTicketFinish(this, uuid);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
