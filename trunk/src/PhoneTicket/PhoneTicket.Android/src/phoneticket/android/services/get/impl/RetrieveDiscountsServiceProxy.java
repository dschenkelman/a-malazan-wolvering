package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveDiscountsServiceDelegate;

public class RetrieveDiscountsServiceProxy implements IRetrieveDiscountService {

	@Override
	public void retrieveDiscounts(IRetrieveDiscountsServiceDelegate delegate) {
		(new RetrieveDiscountsService()).retrieveDiscounts(delegate);
	}

}
