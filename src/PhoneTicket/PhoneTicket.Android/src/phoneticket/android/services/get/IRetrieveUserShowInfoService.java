package phoneticket.android.services.get;

public interface IRetrieveUserShowInfoService {

	void retrieveUserShowInfo(IRetrieveUserShowInfoServiceDelegate delegate,
			int userShowId);
}
