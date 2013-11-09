package phoneticket.android.activities;

import java.util.Arrays;

import com.google.inject.Inject;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.ProgressDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.activities.dialog.ProgressDialogFragment.IProgressDialogDataSource;
import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;
import phoneticket.android.utils.UserManager;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectResource;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;

public class LoginActivity extends RoboFragmentActivity implements
		IAuthServiceDelegate, IMessageDialogDataSource,
		IProgressDialogDataSource {

	@Inject
	private IAuthService service;
	@Inject
	private IFormValidator loginForm;

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
		setContentView(R.layout.activity_login);
		setupActionBar();
		createLoginForm();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
				&& actionBar != null) {

			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.default_action_bar, null);
			((TextView) v.findViewById(R.id.actionTitle))
					.setText(getResources().getString(
							R.string.title_activity_login));

			actionBar.setCustomView(v);
		}
	}

	private void createLoginForm() {
		Validate emailField = new Validate(
				(EditText) findViewById(R.id.inputEmail));
		Validate passwordField = new Validate(
				(EditText) findViewById(R.id.inputPassword));

		Context context = getApplicationContext();

		emailField.addValidator(new NotEmptyValidator(context));
		passwordField.addValidator(new NotEmptyValidator(context));

		emailField.addValidator(new EmailValidator(context));

		loginForm.addValidates(emailField);
		loginForm.addValidates(passwordField);
	}

	public void onLoginButtonAction(View sender) {
		if (loginForm.validate()) {
			hideKeyboard();
			showProgressDialog();
			LoginUser loginUser = createLoginUser();
			service.authUser(this, loginUser);
		}
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		EditText field = (EditText) findViewById(R.id.inputEmail);
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

	private LoginUser createLoginUser() {
		String email = ((EditText) findViewById(R.id.inputEmail)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.inputPassword))
				.getText().toString();
		return new LoginUser(email, password);
	}

	public void onRegisterButtonAction(View sender) {
		Intent intent = new Intent(this, RegisterUserActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public void authServiceDelegateFinish(IAuthService service, LoginUser user) {
		hideProgressDialog();
		Intent resultData = new Intent();
		setResult(MasterActivity.LOGIN_RESULT_CODE_OK, resultData);
		UserManager.getInstance().setCredentials(user.getEmail(),
				user.getPassword());
		finish();
	}

	@Override
	public void authServiceDelegateFinishWithError(IAuthService service,
			Integer errorCode) {
		hideProgressDialog();
		lastMessageTitle = "Error";
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
	public String getProgressMessageTitle() {
		return "Espera un momento";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent resultData = new Intent();
		setResult(0, resultData);
		finish();
	}

}
