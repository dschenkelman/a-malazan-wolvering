package phoneticket.android.services.get;

public interface IRetrieveCinemaInfoService {
	
	void retrieveCinemaInfo(
			IRetrieveCinemaInfoServiceDelegate delegate, int cinemaId);
}
