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
import org.robolectric.shadows.ShadowPreferenceManager;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.utils.UserManager;
import phoneticket.android.validator.IFormValidator;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

	@Mock
	private IAuthService authService;
	@Mock
	private IFormValidator formValidator;
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
		activity = Robolectric.buildActivity(LoginActivity.class).create()
				.get();
		login = (Button) activity.findViewById(R.id.loginButton);
		user = (EditText) activity.findViewById(R.id.inputEmail);
		user.setText("gfesta@gmail.com");
		password = (EditText) activity.findViewById(R.id.inputPassword);
		password.setText("password");
		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);
	}

	@Test
	public void loginActivityCallFormValidator() {
		login.performClick();
		Mockito.verify(formValidator, Mockito.times(1)).validate();
	}

	@Test
	public void loginActivityCallAuthService() {
		Mockito.when(formValidator.validate()).thenReturn(true);
		login.performClick();
		Mockito.verify(authService, Mockito.times(1)).authUser(
				activity,
				new LoginUser(user.getText().toString(), password.getText()
						.toString()));
	}

	@Test
	public void loginActivityNotCallAuthService() {
		Mockito.when(formValidator.validate()).thenReturn(false);
		login.performClick();
		Mockito.verify(authService, Mockito.times(0)).authUser(
				activity,
				new LoginUser(user.getText().toString(), password.getText()
						.toString()));
	}
}
