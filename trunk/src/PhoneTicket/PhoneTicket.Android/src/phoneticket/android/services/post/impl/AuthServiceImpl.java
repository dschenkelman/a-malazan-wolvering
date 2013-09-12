package phoneticket.android.services.post.impl;

import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;

public class AuthServiceImpl implements IAuthService{

	@Override
	public void authUser(IAuthServiceDelegate delegate, LoginUser user) {
		if(user.getEmail().equals("mservetto@gmail.com") && user.getPassword().equals("password")) {
			delegate.authServiceDelegateFinish(this, user);
			return;
		}
		if(user.getEmail().equals("srodriguez@gmail.com") && user.getPassword().equals("password")) {
			delegate.authServiceDelegateFinish(this, user);
			return;
		}
		if(user.getEmail().equals("dschenkelman@gmail.com") && user.getPassword().equals("password")) {
			delegate.authServiceDelegateFinish(this, user);
			return;
		}
		if(user.getEmail().equals("gfesta@gmail.com") && user.getPassword().equals("password")) {
			delegate.authServiceDelegateFinish(this, user);
			return;
		}
		delegate.authServiceDelegateFinishWithError(this, "Credenciales inválidas");		
	}

}
