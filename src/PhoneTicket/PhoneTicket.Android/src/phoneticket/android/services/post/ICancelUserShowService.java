package phoneticket.android.services.post;

import phoneticket.android.model.IDetailUserShow;

public interface ICancelUserShowService {

	void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow);
	
}
