package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.model.CreditCard;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveCreditCardsServise;
import phoneticket.android.services.get.IRetrieveCreditCardsServiseDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveCreditCardsServise extends GetService implements
		IRetrieveCreditCardsServise {

	private IRetrieveCreditCardsServiseDelegate delegate;

	public RetrieveCreditCardsServise() {
		performingRequest = false;
	}

	@Override
	public void retrieveCreditCards(
			IRetrieveCreditCardsServiseDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveCreditCardsServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		List<CreditCard> creditCards = new ArrayList<CreditCard>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveCreditCardsFinishedWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMovieListItems = new JSONArray(result);
				for (int i = 0; i < jsonMovieListItems.length(); i++) {
					JSONObject jsonMovieListItem = jsonMovieListItems
							.getJSONObject(i);
					CreditCard creditCard = new Gson().fromJson(
							jsonMovieListItem.toString(), CreditCard.class);
					if (null != creditCard) {
						creditCards.add(creditCard);
					}
				}
				delegate.retrieveCreditCardsFinished(this, creditCards);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveCreditCardsFinishedWithError(this, 2);
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
