package phoneticket.android.services.get.mock;

import java.util.ArrayList;

import phoneticket.android.model.Cinema;
import phoneticket.android.model.ICinema;
import phoneticket.android.model.Location;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;

public class MockRetrieveCinemaListService implements
		IRetrieveCinemaListService {

	@Override
	public void retrieveCinemaList(IRetrieveCinemaListServiceDelegate delegate) {
		ArrayList<ICinema> cinemas = new ArrayList<ICinema>();
		Cinema cinema1 = new Cinema(0, "El Gran Cine", "Lavalle 1520",
				new Location(123.3, 130.0));
		Cinema cinema2 = new Cinema(1, "El Gran Cine 2", "Moreno 1520",
				new Location(123123.123, 123123.0));
		cinemas.add(cinema1);
		cinemas.add(cinema2);
		delegate.retrieveCinemaListFinish(this, cinemas);
	}

}
