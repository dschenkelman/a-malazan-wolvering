package phoneticket.android.services.post;

public interface IRegisterReservationServiceDelegate {

	public void reserveTicketFinish(IRegisterReservationService service, String uuid);

	public void reserveTicketFinishWithError(IRegisterReservationService service,
			int statusCode);
}
