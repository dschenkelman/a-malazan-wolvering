package phoneticket.android.services.post;

import phoneticket.android.model.LoginUser;

public interface IAuthService
{
	void authUser(IAuthServiceDelegate delegate, LoginUser user);
}
