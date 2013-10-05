package phoneticket.android.services.get;

import java.util.Collection;

import phoneticket.android.model.ICinema;

public interface IRetrieveCinemaListServiceDelegate {
	
	void retrieveCinemaListFinish(IRetrieveCinemaListService service,
			Collection<ICinema> cinemas);

	void retrieveCinemaListFinishWithError(
			IRetrieveCinemaListService service, Integer errorCode);
}
