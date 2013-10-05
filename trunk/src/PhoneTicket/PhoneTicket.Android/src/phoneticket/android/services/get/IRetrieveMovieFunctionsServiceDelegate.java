package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.IMovieFunctions;

public interface IRetrieveMovieFunctionsServiceDelegate {

	void retrieveMovieFunctionsFinish(IRetrieveMovieFunctionsService service,
			Collection<IMovieFunctions> movieFunctions);

	void retrieveMovieFunctionsFinishWithError(
			IRetrieveMovieFunctionsService service, Integer errorCode);
}
