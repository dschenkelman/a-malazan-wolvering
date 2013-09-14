package phoneticket.android.activities;

import java.util.Arrays;

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
import phoneticket.android.activities.dialog.ProgressDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.activities.dialog.ProgressDialogFragment.IProgressDialogDataSource;
import phoneticket.android.model.IUser;
import phoneticket.android.model.User;
import phoneticket.android.services.post.IRegisterUserService;
import phoneticket.android.services.post.IRegisterUserServiceDelegate;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectResource;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class RegisterUserActivity extends RoboFragmentActivity implements
		IRegisterUserServiceDelegate, IMessageDialogDataSource,
		IConfirmUserRegisterDialogDelegate, IProgressDialogDataSource {

	@Inject
	private IFormValidator registerForm;
	@Inject
	private IRegisterUserService service;

	@InjectResource(R.string.defaulErrorMessage)
	private String lastMesage;
	private String lastMessageTitle;

	@InjectResource(R.array.codes)
	private String[] codes;

	@InjectResource(R.array.codesNames)
	private String[] codesNames;

	private ProgressDialogFragment progressDialog;

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
			hideKeyboard();
			showProgressDialog();
			User user = generateUser();
			service.registerUser(user, this);
		}
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		EditText field = (EditText) findViewById(R.id.inputBirthday);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputCellPhone);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputConfirmPassword);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputDNI);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputEmail);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputLastName);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputFirstName);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
		field = (EditText) findViewById(R.id.inputPassword);
		imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
	}
	
	private void showProgressDialog() {
		if (null == progressDialog)
			progressDialog = new ProgressDialogFragment();
		progressDialog.show(getSupportFragmentManager(), "dialog.progress");
	}

	private void hideProgressDialog() {
		progressDialog.dismiss();
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
		hideProgressDialog();
		ConfirmUserRegisterDialogFragment dialog = new ConfirmUserRegisterDialogFragment();
		dialog.show(getSupportFragmentManager(), "dialog.confirmuser");
	}

	@Override
	public void registerUserFinishWithError(IRegisterUserService service,
			Integer errorCode) {
		Log.d("PhoneTicket", "registerUserFinishWithError");
		hideProgressDialog();
		Integer index = Arrays.asList(codes).indexOf(errorCode.toString());
		if (index != null)
			lastMesage = codesNames[index];
		else
			lastMesage = getResources().getString(R.string.defaulErrorMessage);
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

	@Override
	public String getProgressMessageTitle() {
		return "Espera un momento";
	}
}
