package phoneticket.android.services.get.mock;

import java.util.ArrayList;

import phoneticket.android.model.Cinema;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;

public class MockRetrieveCinemaListService implements
		IRetrieveCinemaListService {

	@Override
	public void retrieveCinemaList(IRetrieveCinemaListServiceDelegate delegate) {
		ArrayList<ICinema> cinemas = new ArrayList<ICinema>();
		Cinema cinema1 = new Cinema(0, "El Gran Cine", "Santa Fe 2500",
				-58.402323, -34.594519);
		Cinema cinema2 = new Cinema(1, "El Gran Cine 2", "Las Heras 1800",
				-58.39183, -34.991816);
		cinemas.add(cinema1);
		cinemas.add(cinema2);
		delegate.retrieveCinemaListFinish(this, cinemas);
	}

}
