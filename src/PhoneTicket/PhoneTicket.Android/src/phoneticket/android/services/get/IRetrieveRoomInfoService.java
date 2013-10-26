package phoneticket.android.services.get;

public interface IRetrieveRoomInfoService {
	void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate, int roomId);
}
