package phoneticket.android.services.factories;

import phoneticket.android.model.LoginUser;
import phoneticket.android.model.User;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;
import phoneticket.android.services.post.impl.AuthService;
import phoneticket.android.services.post.impl.RegisterUserService;

public class ServicesFactory {

	static private boolean debugmode = false;
	
	public static IRegisterUserService createRegisterUserService() {
		
		if(debugmode) {
			return new IRegisterUserService() {
				@Override
				public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
					if(user.getEmail().equals("mservetto@gmail.com") || 
							user.getEmail().equals("srodriguez@gmail.com") ||
							user.getEmail().equals("dschenkelman@gmail.com") ||
							user.getEmail().equals("gfesta@gmail.com")) {
						delegate.registerUserFinishWithError(this, "E-mail en uso.");
						return;
					}
					if(1111 == user.getDni()) {
						delegate.registerUserFinishWithError(this, 
								"La persona con DNI " + user.getDni() + " ya posee cuenta");
						return;
					}
					delegate.registerUserFinish(this, user);
				}
			};
		} else {
			return new RegisterUserService();
		}
	}
	
	public static IAuthService createAuthService() {
		if(debugmode) {
			return new IAuthService() {
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
			};
		} else {
			return new AuthService();
		}
	}
}
