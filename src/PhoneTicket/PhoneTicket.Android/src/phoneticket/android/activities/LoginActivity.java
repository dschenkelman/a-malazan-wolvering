package phoneticket.android.activities;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import phoneticket.android.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class LoginActivity extends Activity {

	private Form loginForm;
	
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
        
		loginForm = new Form();
		loginForm.addValidates(emailField);
		loginForm.addValidates(passwordField);
	}

	public void onLoginButtonAction(View sender) {
		if(loginForm.validate()) {
			Log.d("PhoneTicket", "login form is valid");
		} else {
			Log.d("PhoneTicket", "login form is not valid");
		}
	}
	
	public void onRegisterButtonAction(View sender) {
		Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
	}
}
