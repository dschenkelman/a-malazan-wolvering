package phoneticket.android.appliaction;

import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.impl.RetrieveMovieFunctionsServiceProxy;
import phoneticket.android.services.get.impl.RetrieveMovieInfoServiceProxy;
import phoneticket.android.services.get.impl.RetrieveMovieListServiceProxy;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.impl.AuthServiceProxy;
import phoneticket.android.services.post.impl.RegisterUserServiceProxy;
import phoneticket.android.validator.FormValidatorImpl;
import phoneticket.android.validator.IFormValidator;

import com.google.inject.Binder;
import com.google.inject.Module;

public class PhoneTicketModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(IAuthService.class).to(AuthServiceProxy.class);
		binder.bind(IRegisterUserService.class).to(
				RegisterUserServiceProxy.class);
		binder.bind(IFormValidator.class).to(FormValidatorImpl.class);
		binder.bind(IRetrieveMovieListService.class).to(
				RetrieveMovieListServiceProxy.class);
		binder.bind(IRetrieveMovieInfoService.class).to(
				RetrieveMovieInfoServiceProxy.class);
		binder.bind(IRetrieveMovieFunctionsService.class).to(
				RetrieveMovieFunctionsServiceProxy.class);
	}

}
