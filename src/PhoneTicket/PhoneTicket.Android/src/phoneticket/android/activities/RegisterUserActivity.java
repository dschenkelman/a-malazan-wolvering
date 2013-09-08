package phoneticket.android.activities;

import com.throrinstudio.android.common.libs.validator.AbstractValidate;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneOrEmptyValidator;

import phoneticket.android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterUserActivity extends Activity {

	private Form registerForm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		createValidationForm();
	}
	
	private void createValidationForm() {
        Validate nameField              = new Validate((EditText)findViewById(R.id.inputFirstName));
        Validate lastNameField       	= new Validate((EditText)findViewById(R.id.inputLastName));
        Validate emailField             = new Validate((EditText)findViewById(R.id.inputEmail));
        Validate dniField           	= new Validate((EditText)findViewById(R.id.inputDNI));
        Validate birthdayField        	= new Validate((EditText)findViewById(R.id.inputBirthday));
        Validate phoneField             = new Validate((EditText)findViewById(R.id.inputPhone));
        Validate cellPhoneField         = new Validate((EditText)findViewById(R.id.inputCellPhone));
        Validate passwordField          = new Validate((EditText)findViewById(R.id.inputPassword));
        Validate confirmPasswordField   = new Validate((EditText)findViewById(R.id.inputConfirmPassword));

        /*
        AbstractValidate phonesFields = new OrTwoRequiredValidate(
                (EditText)findViewById(R.id.inputPhone),
                (EditText)findViewById(R.id.inputCellPhone));*/
        AbstractValidate confirmFields = new ConfirmValidate(
                (EditText)findViewById(R.id.inputPassword),
                (EditText)findViewById(R.id.inputConfirmPassword));

        Context context = getApplicationContext();

        nameField.addValidator(new NotEmptyValidator(context));
        lastNameField.addValidator(new NotEmptyValidator(context));
        emailField.addValidator(new NotEmptyValidator(context));
        dniField.addValidator(new NotEmptyValidator(context));
        passwordField.addValidator(new NotEmptyValidator(context));
        confirmPasswordField.addValidator(new NotEmptyValidator(context));

        emailField.addValidator(new EmailValidator(context));

        
        phoneField.addValidator(new PhoneOrEmptyValidator(context));
        cellPhoneField.addValidator(new PhoneOrEmptyValidator(context));

        registerForm = new Form();
        registerForm.addValidates(nameField);
        registerForm.addValidates(lastNameField);
        registerForm.addValidates(emailField);
        registerForm.addValidates(dniField);
        registerForm.addValidates(birthdayField);
        registerForm.addValidates(phoneField);
        registerForm.addValidates(cellPhoneField);
        registerForm.addValidates(passwordField);
        registerForm.addValidates(confirmPasswordField);
        registerForm.addValidates(confirmFields);
	}

	public void onRegisterButtonAction(View sender) {
		if(registerForm.validate()) {
			Log.d("PhoneTicket", "form is valid");
		} else {
			Log.d("PhoneTicket", "form is not valid");
		}
	}
}
