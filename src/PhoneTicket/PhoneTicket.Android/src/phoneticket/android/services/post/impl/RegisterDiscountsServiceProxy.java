package phoneticket.android.services.post.impl;

import java.util.List;

import phoneticket.android.model.PostedDiscounts;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterDiscountsServiceDelegate;

public class RegisterDiscountsServiceProxy implements IRegisterDiscountsService {

	@Override
	public void registerDiscounts(IRegisterDiscountsServiceDelegate delegate,
			List<PostedDiscounts> discounts, String uuid) {
		(new RegisterDiscountsService()).registerDiscounts(delegate, discounts,
				uuid);

	}

}
