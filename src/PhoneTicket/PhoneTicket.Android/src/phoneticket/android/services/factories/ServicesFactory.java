package phoneticket.android.services.factories;

import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;

public class ServicesFactory {

	public static IRegisterUserService createRegisterUserService() {
		return new IRegisterUserService() {
			@Override
			public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
				delegate.registerUserFinish(this, user);
			}
		};
	}
}
