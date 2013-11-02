package phoneticket.android.services.delete;

public interface ICancelUserShowServiceDelegate {

	void cancelUserShowFinished(ICancelUserShowService delegate);

	void cancelUserShowFinishedWithError(ICancelUserShowService delegate,
			int errorCode);

}
