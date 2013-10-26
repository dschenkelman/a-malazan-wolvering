package phoneticket.android.appliaction;

import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.impl.RetrieveCinemaInfoServiceProxy;
import phoneticket.android.services.get.impl.RetrieveCinemaListServiceProxy;
import phoneticket.android.services.get.impl.RetrieveMovieFunctionsServiceProxy;
import phoneticket.android.services.get.impl.RetrieveMovieInfoServiceProxy;
import phoneticket.android.services.get.impl.RetrieveMovieListServiceProxy;
import phoneticket.android.services.get.mock.MockRetrieveUserInfoService;
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
		binder.bind(IRetrieveCinemaListService.class).to(
				RetrieveCinemaListServiceProxy.class);
		binder.bind(IRetrieveCinemaInfoService.class).to(
				RetrieveCinemaInfoServiceProxy.class);
		binder.bind(IRetrieveUserInfoService.class).to(
				MockRetrieveUserInfoService.class);
	}

}
