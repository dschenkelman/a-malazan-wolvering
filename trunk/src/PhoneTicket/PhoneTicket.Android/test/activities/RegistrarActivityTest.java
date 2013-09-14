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

import phoneticket.android.R;
import phoneticket.android.activities.RegisterUserActivity;
import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.validator.IFormValidator;
import android.widget.Button;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class RegistrarActivityTest {

	@Mock
	private IFormValidator formValidator;
	@Mock
	private IRegisterUserService registerService;
	private RegisterUserActivity activity;
	private Button register;
	private EditText password;
	private EditText firstName;
	private EditText lastName;
	private EditText email;
	private EditText dni;
	private EditText birthday;
	private EditText cellPhone;
	private User userRegister;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();
		module.addBinding(IFormValidator.class, formValidator);
		module.addBinding(IRegisterUserService.class, registerService);
		TestModule.setUp(this, module);
		activity = Robolectric.buildActivity(RegisterUserActivity.class)
				.create().get();
		register = (Button) activity.findViewById(R.id.registerButton);
		firstName = (EditText) activity.findViewById(R.id.inputFirstName);
		firstName.setText("Carlos");
		lastName = (EditText) activity.findViewById(R.id.inputLastName);
		lastName.setText("Perez");
		email = (EditText) activity.findViewById(R.id.inputEmail);
		email.setText("carlos.perez@gmail.com");
		dni = (EditText) activity.findViewById(R.id.inputDNI);
		dni.setText("33345123");
		birthday = (EditText) activity.findViewById(R.id.inputBirthday);
		birthday.setText("09/09/87");
		cellPhone = (EditText) activity.findViewById(R.id.inputCellPhone);
		cellPhone.setText("155555555");
		password = (EditText) activity.findViewById(R.id.inputPassword);
		password.setText("1234");
		
		userRegister = new User(firstName.getText().toString(), lastName
				.getText().toString(), email.getText().toString(),
				Integer.parseInt(dni.getText().toString()), birthday
						.getText().toString(), cellPhone.getText().toString(),
				password.getText().toString());
	}

	@Test
	public void registrarActivityCallFormValidator(){
		register.performClick();
		Mockito.verify(formValidator,Mockito.times(1)).validate();
	}
	
	@Test
	public void registerActivityCallRegisterUserService() {
		Mockito.when(formValidator.validate()).thenReturn(true);
		register.performClick();
		Mockito.verify(registerService, Mockito.times(1)).registerUser(
				userRegister, activity);
	}
	
	@Test
	public void registerActivityNotCallRegisterUserService() {
		Mockito.when(formValidator.validate()).thenReturn(false);
		register.performClick();
		Mockito.verify(registerService, Mockito.times(0)).registerUser(
				userRegister, activity);
	}

}
