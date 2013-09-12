package phoneticket.android.activities;

import com.google.inject.Inject;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.model.LoginUser;
import phoneticket.android.services.post.IAuthService;
import phoneticket.android.services.post.IAuthServiceDelegate;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;

public class LoginActivity extends RoboFragmentActivity implements 
	IAuthServiceDelegate, IMessageDialogDataSource {

	@Inject private IAuthService service;
	@Inject private IFormValidator loginForm;
	private String lastMesage;
	private String lastMessageTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		createLoginForm();
	}
	
	private void createLoginForm() {
        Validate emailField		= new Validate((EditText)findViewById(R.id.inputEmail));
        Validate passwordField  = new Validate((EditText)findViewById(R.id.inputPassword));

        Context context = getApplicationContext();
        
        emailField.addValidator(new NotEmptyValidator(context));
        passwordField.addValidator(new NotEmptyValidator(context));

        emailField.addValidator(new EmailValidator(context));
        
		loginForm.addValidates(emailField);
		loginForm.addValidates(passwordField);
	}

	public void onLoginButtonAction(View sender) {
		if(loginForm.validate()) {
			LoginUser loginUser = createLoginUser();
			service.authUser(this, loginUser);
		}
	}
	
	private LoginUser createLoginUser() {
        String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
		return new LoginUser(email, password);
	}

	public void onRegisterButtonAction(View sender) {
		Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
	}

	@Override
	public void authServiceDelegateFinish(IAuthService service, LoginUser user) {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void authServiceDelegateFinishWithError(IAuthService service,
			String errorMessage) {
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

}
