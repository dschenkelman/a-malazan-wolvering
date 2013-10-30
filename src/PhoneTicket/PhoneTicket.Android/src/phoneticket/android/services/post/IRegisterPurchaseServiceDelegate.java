package phoneticket.android.services.post;

public interface IRegisterPurchaseServiceDelegate {

	public void purchaseTicketFinish(IRegisterPurchaseService service, String uuid);

	public void purchaseTicketFinishWithError(IRegisterPurchaseService service,
			int statusCode);
}
