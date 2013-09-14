package phoneticket.android.activities;

import com.google.inject.Inject;
import com.throrinstudio.android.common.libs.validator.AbstractValidate;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneOrEmptyValidator;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.ConfirmUserRegisterDialogFragment.IConfirmUserRegisterDialogDelegate;
import phoneticket.android.activities.dialog.ConfirmUserRegisterDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.model.IUser;
import phoneticket.android.model.User;
import phoneticket.android.services.factories.ServicesFactory;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterUserActivity extends RoboFragmentActivity implements
		IRegisterUserServiceDelegate, IMessageDialogDataSource,
		IConfirmUserRegisterDialogDelegate {

	@Inject
	private IFormValidator registerForm;
	@Inject
	private IRegisterUserService service;
	private String lastMesage;
	private String lastMessageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);

		createValidationForm();
	}

	private void createValidationForm() {
		Validate nameField = new Validate(
				(EditText) findViewById(R.id.inputFirstName));
		Validate lastNameField = new Validate(
				(EditText) findViewById(R.id.inputLastName));
		Validate emailField = new Validate(
				(EditText) findViewById(R.id.inputEmail));
		Validate dniField = new Validate((EditText) findViewById(R.id.inputDNI));
		Validate birthdayField = new Validate(
				(EditText) findViewById(R.id.inputBirthday));
		Validate cellPhoneField = new Validate(
				(EditText) findViewById(R.id.inputCellPhone));
		Validate passwordField = new Validate(
				(EditText) findViewById(R.id.inputPassword));
		Validate confirmPasswordField = new Validate(
				(EditText) findViewById(R.id.inputConfirmPassword));

		AbstractValidate confirmFields = new ConfirmValidate(
				(EditText) findViewById(R.id.inputPassword),
				(EditText) findViewById(R.id.inputConfirmPassword));

		Context context = getApplicationContext();

		nameField.addValidator(new NotEmptyValidator(context));
		lastNameField.addValidator(new NotEmptyValidator(context));
		emailField.addValidator(new NotEmptyValidator(context));
		dniField.addValidator(new NotEmptyValidator(context));
		passwordField.addValidator(new NotEmptyValidator(context));
		confirmPasswordField.addValidator(new NotEmptyValidator(context));

		emailField.addValidator(new EmailValidator(context));

		cellPhoneField.addValidator(new PhoneOrEmptyValidator(context));

		registerForm.addValidates(nameField);
		registerForm.addValidates(lastNameField);
		registerForm.addValidates(emailField);
		registerForm.addValidates(dniField);
		registerForm.addValidates(birthdayField);
		registerForm.addValidates(cellPhoneField);
		registerForm.addValidates(passwordField);
		registerForm.addValidates(confirmPasswordField);
		registerForm.addValidates(confirmFields);
	}

	public void onRegisterButtonAction(View sender) {
		if (registerForm.validate()) {
			User user = generateUser();
			service = ServicesFactory.createRegisterUserService();
			service.registerUser(user, this);
		}
	}

	private User generateUser() {
		String name = ((EditText) findViewById(R.id.inputFirstName)).getText()
				.toString();
		String lastName = ((EditText) findViewById(R.id.inputLastName))
				.getText().toString();
		String email = ((EditText) findViewById(R.id.inputEmail)).getText()
				.toString();
		String dni = ((EditText) findViewById(R.id.inputDNI)).getText()
				.toString();
		String birthday = ((EditText) findViewById(R.id.inputBirthday))
				.getText().toString();
		String cellPhone = ((EditText) findViewById(R.id.inputCellPhone))
				.getText().toString();
		String password = ((EditText) findViewById(R.id.inputPassword))
				.getText().toString();

		User user = new User(name, lastName, email, Integer.parseInt(dni),
				birthday, cellPhone, password);
		return user;
	}

	@Override
	public void registerUserFinish(IRegisterUserService service, IUser user) {
		Log.d("PhoneTicket", "registerUserFinish");
		ConfirmUserRegisterDialogFragment dialog = new ConfirmUserRegisterDialogFragment();
		dialog.show(getSupportFragmentManager(), "dialog.confirmuser");
	}

	@Override
	public void registerUserFinishWithError(IRegisterUserService service,
			String errorMessage) {
		Log.d("PhoneTicket", "registerUserFinishWithError");
		lastMesage = errorMessage;
		lastMessageTitle = "Error";
		MessageDialogFragment dialog = new MessageDialogFragment();
		dialog.show(getSupportFragmentManager(), "dialog.error");
	}

	@Override
	public String getMessage() {
		return lastMesage;
	}

	@Override
	public String getMessageTitle() {
		return lastMessageTitle;
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		onBackPressed();
	}
}
