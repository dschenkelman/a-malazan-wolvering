package phoneticket.android.services.factories;

import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.impl.AuthServiceImpl;

import com.google.inject.Provider;

public class AuthServiceProvider implements Provider<IAuthService> {

	@Override
	public IAuthService get() {
		return new AuthServiceImpl();
	}

}
