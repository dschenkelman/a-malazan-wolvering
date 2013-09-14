package phoneticket.android.services.post.impl;

import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;

public class AuthServiceProxy implements IAuthService {

	// Se Hace de esta manera porque un AsynTask en Android solo puede ser
	// ejecutada una vez
	@Override
	public void authUser(IAuthServiceDelegate delegate, LoginUser user) {
		(new AuthService()).authUser(delegate, user);
	}

}
