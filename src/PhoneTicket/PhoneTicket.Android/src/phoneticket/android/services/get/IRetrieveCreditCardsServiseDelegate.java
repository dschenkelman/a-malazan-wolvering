package phoneticket.android.services.get;

import java.util.List;

import phoneticket.android.model.CreditCard;

public interface IRetrieveCreditCardsServiseDelegate {

	void retrieveCreditCardsFinished(IRetrieveCreditCardsServise service,
			List<CreditCard> creditCards);
	
	void retrieveCreditCardsFinishedWithError(IRetrieveCreditCardsServise service,
			int errorCode);
}
