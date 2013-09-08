package phoneticket.android.services.factories;

import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;

public class ServicesFactory {

	public static IRegisterUserService createRegisterUserService() {
		return new IRegisterUserService() {
			@Override
			public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
				
				if(user.getEmail().equals("matias@gmail.com")) {
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
	}
}
