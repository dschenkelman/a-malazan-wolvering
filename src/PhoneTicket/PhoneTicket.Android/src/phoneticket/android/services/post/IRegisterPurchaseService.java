package phoneticket.android.services.post;

import phoneticket.android.model.PostedTicket;

public interface IRegisterPurchaseService {

	public void purchaseTicket(IRegisterPurchaseServiceDelegate delegate,
			PostedTicket ticket);

}
