package phoneticket.android.services.post.impl;

import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;

public class RegisterUserService implements IRegisterUserService {
	@Override
	public void registerUser(User user, IRegisterUserServiceDelegate delegate) {
		if (user.getEmail().equals("mservetto@gmail.com")
				|| user.getEmail().equals("srodriguez@gmail.com")
				|| user.getEmail().equals("dschenkelman@gmail.com")
				|| user.getEmail().equals("gfesta@gmail.com")) {
			delegate.registerUserFinishWithError(this, "E-mail en uso.");
			return;
		}
		if (1111 == user.getDni()) {
			delegate.registerUserFinishWithError(this, "La persona con DNI "
					+ user.getDni() + " ya posee cuenta");
			return;
		}
		delegate.registerUserFinish(this, user);
	}
}
