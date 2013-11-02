package phoneticket.android.services.delete.impl;

import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.delete.ICancelUserShowServiceDelegate;

public class CancelUserShowServiceProxy implements ICancelUserShowService {

	@Override
	public void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow) {
		(new CancelUserShowService()).cancelUserShow(delegate, userShow);
	}

}
