package phoneticket.android.services.post;

import phoneticket.android.model.CreditCardData;

public interface IConfirmReservationService {

	void confirmReservation(IConfirmReservationServiceDelegate delegate,
			CreditCardData data, String reservationId);
}
