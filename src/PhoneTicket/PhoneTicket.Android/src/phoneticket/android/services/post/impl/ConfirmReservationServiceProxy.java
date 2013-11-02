package phoneticket.android.services.post.impl;

import phoneticket.android.model.CreditCardData;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.services.post.IConfirmReservationServiceDelegate;

public class ConfirmReservationServiceProxy implements
		IConfirmReservationService {

	@Override
	public void confirmReservation(IConfirmReservationServiceDelegate delegate,
			CreditCardData data, String reservationId) {
		(new ConfirmReservationService()).confirmReservation(delegate, data,
				reservationId);
	}
}
