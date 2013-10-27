package phoneticket.android.services.get;

import phoneticket.android.model.IDetailUserShow;

public interface IRetrieveUserShowInfoServiceDelegate {

	void retrieveUserShowInfoFinish(IRetrieveUserShowInfoService delegate,
			IDetailUserShow userShow);

	void retrieveUserShowInfoFinishWithError(
			IRetrieveUserShowInfoService delegate, int errorCode);

}
