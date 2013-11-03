package phoneticket.android.services.post.impl;

import java.util.List;

import com.google.gson.Gson;

import phoneticket.android.model.PostedDiscounts;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterDiscountsServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class RegisterDiscountsService extends PostService implements
		IRegisterDiscountsService {

	private List<PostedDiscounts> postObject;
	private IRegisterDiscountsServiceDelegate delegate;

	public RegisterDiscountsService() {
		performingRequest = false;
	}

	@Override
	public void registerDiscounts(IRegisterDiscountsServiceDelegate delegate,
			List<PostedDiscounts> discounts, String uuid) {
		if (true == performingRequest)
			return;
		if (null == delegate || null == discounts)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = discounts;
		connectionSuccess = false;
		execute(APIService.getRegisterDiscountsPostURL(uuid));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (200 != statusLine.getStatusCode()) {
				delegate.registerDiscountsFinishWithError(this,
						statusLine.getStatusCode());
			} else {
				delegate.registerDiscountsFinish(this);
			}
		} else {
			delegate.registerDiscountsFinishWithError(this, 0);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postObject);
		return jsonString;
	}
}
