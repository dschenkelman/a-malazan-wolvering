package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaInfoServiceDelegate;

public class RetrieveCinemaInfoServiceProxy implements
		IRetrieveCinemaInfoService {

	@Override
	public void retrieveCinemaInfo(IRetrieveCinemaInfoServiceDelegate delegate,
			int cinemaId) {
		(new RetrieveCinemaInfoService())
				.retrieveCinemaInfo(delegate, cinemaId);
	}
}
