package phoneticket.android.services.post;

import phoneticket.android.model.LoginUser;


public interface IAuthServiceDelegate
{
	void authServiceDelegateFinish(IAuthService service, LoginUser user);
	void authServiceDelegateFinishWithError(IAuthService service, String errorMessage);
}
