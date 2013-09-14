package phoneticket.android.services.post.impl;

import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;

public class RegisterUserServiceProxy implements IRegisterUserService {

	// Se Hace de esta manera porque un AsynTask en Android solo puede ser
	// ejecutada una vez
	@Override
	public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
		(new RegisterUserService()).registerUser(user, delegate);
	}

}
