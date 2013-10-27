package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.IMyShow;

public interface IRetrieveMyShowsServiceDelegate {

	void retrieveMyShowsServiceFinished(IRetrieveMyShowsService service,
			Collection<IMyShow> myShows);

	void retrieveMyShowsServiceFinishedWithError(
			IRetrieveMyShowsService service, int errorCode);

}
