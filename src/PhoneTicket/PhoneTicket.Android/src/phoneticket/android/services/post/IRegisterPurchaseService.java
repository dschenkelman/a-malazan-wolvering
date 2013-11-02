package phoneticket.android.services.post;

import phoneticket.android.model.PurchaseTicket;

public interface IRegisterPurchaseService {

	public void purchaseTicket(IRegisterPurchaseServiceDelegate delegate,
			PurchaseTicket ticket);

}
