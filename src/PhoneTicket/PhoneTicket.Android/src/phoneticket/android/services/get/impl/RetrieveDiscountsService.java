package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.Discount;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveDiscountsServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveDiscountsService extends GetService implements
		IRetrieveDiscountService {

	private IRetrieveDiscountsServiceDelegate delegate;

	public RetrieveDiscountsService() {
		performingRequest = false;
	}

	@Override
	public void retrieveDiscounts(IRetrieveDiscountsServiceDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveDiscountsListServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Collection<Discount> discounts = new ArrayList<Discount>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveDiscountsFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMovieListItems = new JSONArray(result);
				for (int i = 0; i < jsonMovieListItems.length(); i++) {
					JSONObject jsonMovieListItem = jsonMovieListItems
							.getJSONObject(i);
					Discount discount = new Gson().fromJson(
							jsonMovieListItem.toString(), Discount.class);
					if (null != discount) {
						discounts.add(discount);
					}
				}
				delegate.retrieveDiscountsFinish(this, discounts);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveDiscountsFinishWithError(this, 2);
			}
		}
		delegate = null;
		performingRequest = false;
	}

	@Override
	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		super.handleStatusCodeNotOk(e, statusCode);
		isStatusOk = false;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		super.handleClientProtocolException(e);
		hasCLientProtocolRecieveException = true;
	}

}
