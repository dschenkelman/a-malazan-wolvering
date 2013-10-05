package phoneticket.android.services.get;

public interface IRetrieveMovieFunctionsService {

	void retrieveMovieFunctions(
			IRetrieveMovieFunctionsServiceDelegate delegate, int movieId);
}
