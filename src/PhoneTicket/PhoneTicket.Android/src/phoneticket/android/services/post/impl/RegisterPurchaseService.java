package phoneticket.android.services.post.impl;

import com.google.gson.Gson;

import phoneticket.android.model.PostedTicket;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterPurchaseServiceDelegate;
import phoneticket.android.services.post.PostService;
import phoneticket.android.utils.APIService;

public class RegisterPurchaseService extends PostService implements
		IRegisterPurchaseService {

	private PostedTicket postObject;
	private IRegisterPurchaseServiceDelegate delegate;

	public RegisterPurchaseService() {
		performingRequest = false;
	}

	@Override
	public void purchaseTicket(IRegisterPurchaseServiceDelegate delegate,
			PostedTicket purchaseTicket) {
		if (true == performingRequest)
			return;
		if (null == delegate || null == purchaseTicket)
			return;
		performingRequest = true;
		this.delegate = delegate;
		postObject = purchaseTicket;
		connectionSuccess = false;
		execute(APIService.getRegisterPurchasePostURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (connectionSuccess) {
			if (201 != statusLine.getStatusCode()) {
				delegate.purchaseTicketFinishWithError(this,
						statusLine.getStatusCode());
			} else {
				delegate.purchaseTicketFinish(this, result);
			}
		} else {
			delegate.purchaseTicketFinishWithError(this, 0);
		}
		performingRequest = false;
		delegate = null;
	}

	@Override
	protected String generatePostBodyObject() {
		String jsonString = new Gson().toJson(postObject, PostedTicket.class);
		return jsonString;
	}
}
