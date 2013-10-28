package phoneticket.android.services.post.impl;

import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.post.ICancelUserShowService;
import phoneticket.android.services.post.ICancelUserShowServiceDelegate;
import phoneticket.android.services.post.mock.MockCancelUserShowService;

public class CancelUserShowServiceProxy implements ICancelUserShowService {

	@Override
	public void cancelUserShow(ICancelUserShowServiceDelegate delegate,
			IDetailUserShow userShow) {
		(new MockCancelUserShowService()).cancelUserShow(delegate, userShow);
	}

}
