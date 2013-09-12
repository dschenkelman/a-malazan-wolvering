package phoneticket.android.appliaction;

import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.impl.AuthServiceImpl;
import phoneticket.android.validator.FormValidatorImpl;
import phoneticket.android.validator.IFormValidator;

import com.google.inject.Binder;
import com.google.inject.Module;

public class PhoneTicketModule implements Module {

	@Override
	public void configure(Binder binder) {
        binder.bind(IAuthService.class).to(AuthServiceImpl.class);
        binder.bind(IFormValidator.class).to(FormValidatorImpl.class);
	}

}
