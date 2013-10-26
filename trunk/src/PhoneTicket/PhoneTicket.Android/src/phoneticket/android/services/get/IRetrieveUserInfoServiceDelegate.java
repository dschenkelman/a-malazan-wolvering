package phoneticket.android.services.get;

public interface IRetrieveUserInfoServiceDelegate {

	void retrieveUserInfoFinish(IRetrieveUserInfoService retrieveUserInfoService);

	void retrieveUserInfoFinishWithError(
			IRetrieveUserInfoService retrieveUserInfoService, int errorCode);

}
