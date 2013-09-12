package activities;

import module.TestModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.widget.Button;
import android.widget.EditText;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.validator.IFormValidator;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
	 
	@Mock private IAuthService authService;
	@Mock private IFormValidator formValidator;
	private LoginActivity activity;
	private Button login;
	private EditText user;
	private EditText password;

	@Before
	  public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    TestModule module = new TestModule();
	    module.addBinding(IAuthService.class, authService);
	    module.addBinding(IFormValidator.class, formValidator);
	    TestModule.setUp(this, module);
		activity = Robolectric.buildActivity(LoginActivity.class).create().get();
		login = (Button) activity.findViewById(R.id.loginButton);
		user = (EditText)activity.findViewById(R.id.inputEmail);
		password = (EditText)activity.findViewById(R.id.inputPassword);
	  }
	
	@Test
	public void loginActivityCallAuthService(){
		user.setText("gfesta@gmail.com");
		password.setText("password");
		Mockito.when(formValidator.validate()).thenReturn(true);
		login.performClick();
		Mockito.verify(authService,Mockito.times(1)).authUser(activity, new LoginUser(user.getText().toString(), password.getText().toString()));
	}
}
