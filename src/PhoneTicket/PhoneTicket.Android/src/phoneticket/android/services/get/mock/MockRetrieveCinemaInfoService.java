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
		Cinema cinema1 = new Cinema(0, "El Gran Cine", "Santa Fe 2500",
				new Location(-58.402323, -34.594519));
		Cinema cinema2 = new Cinema(1, "El Gran Cine 2", "Las Heras 1800",
				new Location(-58.39183, -34.591816));
		if (cinemaId == 0) {
			delegate.retrieveCinemaInfoFinish(this, cinema1);
		} else {
			delegate.retrieveCinemaInfoFinish(this, cinema2);
		}
	}
}
