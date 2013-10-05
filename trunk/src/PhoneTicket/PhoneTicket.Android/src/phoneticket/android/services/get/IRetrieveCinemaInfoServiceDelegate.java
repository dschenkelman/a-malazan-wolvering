package phoneticket.android.services.get;

import phoneticket.android.model.ICinema;

public interface IRetrieveCinemaInfoServiceDelegate {

	void retrieveCinemaInfoFinish(IRetrieveCinemaInfoService service,
			ICinema cinema);

	void retrieveCinemaInfoFinishWithError(
			IRetrieveCinemaInfoService service, Integer errorCode);
}
