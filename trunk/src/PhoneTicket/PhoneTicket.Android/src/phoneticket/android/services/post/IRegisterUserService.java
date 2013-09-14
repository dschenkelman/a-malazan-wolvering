package phoneticket.android.services.post;

import phoneticket.android.model.User;

public interface IRegisterUserService {

	public void registerUser(User user, IRegisterUserServiceDelegate delegate);

}
