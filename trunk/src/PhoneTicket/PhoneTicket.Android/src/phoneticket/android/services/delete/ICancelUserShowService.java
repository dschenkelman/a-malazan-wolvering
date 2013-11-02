package phoneticket.android.services.delete;

import phoneticket.android.model.IDetailUserShow;

public interface ICancelUserShowService {

	void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow);
	
}
