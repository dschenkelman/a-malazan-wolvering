package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;
import phoneticket.android.services.get.mock.MockRetrieveRoomInfoService;

public class RetrieveRoomInfoServiceProxy implements IRetrieveRoomInfoService {

	@Override
	public void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate,
			int roomId) {
		(new MockRetrieveRoomInfoService()).retrieveRoomInfo(delegate, roomId);
	}

}
