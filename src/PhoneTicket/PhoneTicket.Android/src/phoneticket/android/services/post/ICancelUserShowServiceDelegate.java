package phoneticket.android.services.post;

public interface ICancelUserShowServiceDelegate {

	void cancelUserShowFinished(ICancelUserShowService delegate);

	void cancelUserShowFinishedWithError(ICancelUserShowService delegate,
			int errorCode);

}
