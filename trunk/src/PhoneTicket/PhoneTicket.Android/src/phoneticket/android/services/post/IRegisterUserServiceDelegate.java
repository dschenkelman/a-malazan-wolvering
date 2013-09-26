package phoneticket.android.services.post;

import phoneticket.android.model.IUser;

public interface IRegisterUserServiceDelegate {

	void registerUserFinish(IRegisterUserService service, IUser user);

	void registerUserFinishWithError(IRegisterUserService service,
			Integer errorCode);

}
