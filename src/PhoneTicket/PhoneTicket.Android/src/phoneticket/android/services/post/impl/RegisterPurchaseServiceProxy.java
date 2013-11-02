package phoneticket.android.services.post.impl;

import phoneticket.android.model.PurchaseTicket;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterPurchaseServiceDelegate;

public class RegisterPurchaseServiceProxy implements IRegisterPurchaseService {

	@Override
	public void purchaseTicket(IRegisterPurchaseServiceDelegate delegate,
			PurchaseTicket ticket) {
		(new RegisterPurchaseService()).purchaseTicket(delegate, ticket);

	}
}
