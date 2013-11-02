package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveCreditCardsServise;
import phoneticket.android.services.get.IRetrieveCreditCardsServiseDelegate;

public class RetrieveCreditCardsServiseProxy implements
		IRetrieveCreditCardsServise {

	@Override
	public void retrieveCreditCards(IRetrieveCreditCardsServiseDelegate delegate) {
		(new RetrieveCreditCardsServise()).retrieveCreditCards(delegate);
	}

}
