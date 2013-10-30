package phoneticket.android.services.post;

import java.util.List;

import phoneticket.android.model.PostedDiscounts;

public interface IRegisterDiscountsService {

	public void registerDiscounts(IRegisterDiscountsServiceDelegate delegate,
			List<PostedDiscounts> discounts, String uuid);
}
