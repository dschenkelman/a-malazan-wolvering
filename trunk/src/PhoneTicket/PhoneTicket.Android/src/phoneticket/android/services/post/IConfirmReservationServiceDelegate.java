package phoneticket.android.services.post;

public interface IConfirmReservationServiceDelegate {

	void confirmReservationFinished(IConfirmReservationService service);

	void confirmReservationFinishedWithError(
			IConfirmReservationService service, int errorCode);
}
