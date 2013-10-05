package phoneticket.android.services.get;

import phoneticket.android.model.IMovieFunctions;

public interface IRetrieveMovieFunctionsServiceDelegate {

	void retrieveMovieFunctionsFinish(IRetrieveMovieFunctionsService service,
			IMovieFunctions movieFunctions);

	void retrieveMovieFunctionsFinishWithError(
			IRetrieveMovieFunctionsService service, Integer errorCode);
}
