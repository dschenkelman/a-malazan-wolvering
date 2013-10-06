package phoneticket.android.services.get.mock;

import phoneticket.android.model.Cinema;
import phoneticket.android.model.Location;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaInfoServiceDelegate;

public class MockRetrieveCinemaInfoService implements
		IRetrieveCinemaInfoService {

	@Override
	public void retrieveCinemaInfo(IRetrieveCinemaInfoServiceDelegate delegate,
			int cinemaId) {

		Cinema cinema1 = new Cinema(0, "El Gran Cine", "Lavalle 1520",
				new Location(123.3, 130.0));
		Cinema cinema2 = new Cinema(1, "El Gran Cine 2", "Moreno 1520",
				new Location(123123.123, 123123.0));
		if (cinemaId == 0) {
			delegate.retrieveCinemaInfoFinish(this, cinema1);
		} else {
			delegate.retrieveCinemaInfoFinish(this, cinema2);
		}
	}

}
