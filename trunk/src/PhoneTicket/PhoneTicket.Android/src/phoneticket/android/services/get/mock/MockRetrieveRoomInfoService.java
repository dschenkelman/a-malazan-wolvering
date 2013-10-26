package phoneticket.android.services.get.mock;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;

public class MockRetrieveRoomInfoService implements IRetrieveRoomInfoService {

	@Override
	public void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate,
			int roomId) {
		Collection<Collection<Integer>> movieList = new LinkedList<Collection<Integer>>();
		Random r = new Random();
		for (int i = 0; i < 17; i++) {
			Collection<Integer> row = new LinkedList<Integer>();
			for (int j = 0; j < 22; j++) {
				Integer random = r.nextInt(3);
				row.add(random);
			}
			movieList.add(row);
		}
		delegate.retrieveRoomInfoFinish(this, movieList);
	}
}
