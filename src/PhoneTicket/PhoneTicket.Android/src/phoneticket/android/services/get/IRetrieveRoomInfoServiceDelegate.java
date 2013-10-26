package phoneticket.android.services.get;

import java.util.Collection;

public interface IRetrieveRoomInfoServiceDelegate {

	void retrieveRoomInfoFinish(IRetrieveRoomInfoService service,
			Collection<Collection<Integer>> movieList);

	void retrieveRoomInfoFinishWithError(IRetrieveRoomInfoService service,
			Integer errorCode);
}
