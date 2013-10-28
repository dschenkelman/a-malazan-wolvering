package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.Discount;

public interface IRetrieveDiscountsServiceDelegate {

	void retrieveDiscountsFinish(
			IRetrieveDiscountService retrieveDiscountsService,
			Collection<Discount> discounts);

	void retrieveDiscountsFinishWithError(
			IRetrieveDiscountService retrieveDiscountsService, int statusCode);

}
