package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveDiscountsServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveDiscountsService;

public class RetrieveDiscountsServiceProxy implements
		IRetrieveDiscountService {

	@Override
	public void retrieveDiscounts(IRetrieveDiscountsServiceDelegate delegate) {
		(new MockRetrieveDiscountsService()).retrieveDiscounts(delegate);
	}

}
