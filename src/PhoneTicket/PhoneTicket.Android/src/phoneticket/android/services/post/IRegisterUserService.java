package phoneticket.android.services.post;

import phoneticket.android.model.User;

public interface IRegisterUserService {

	void registerUser(User user, IRegisterUserServiceDelegate delegate);

}
