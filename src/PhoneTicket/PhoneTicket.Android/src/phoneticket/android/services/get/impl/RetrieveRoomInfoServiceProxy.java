package phoneticket.android.services.get.impl;

import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;

public class RetrieveRoomInfoServiceProxy implements IRetrieveRoomInfoService {

	@Override
	public void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate,
			int functionId) {
		(new RetrieveRoomInfoService()).retrieveRoomInfo(delegate, functionId);
	}

}
