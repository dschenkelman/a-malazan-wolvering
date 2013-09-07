package sample.validatorapp;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.throrinstudio.android.common.libs.validator.AbstractValidate;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validate.OrTwoRequiredValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneOrEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneValidator;

public class MainActivity extends Activity {

    private Form registerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRegisterValidatorForm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void createRegisterValidatorForm() {

        Validate nameField              = new Validate((EditText)findViewById(R.id.inputName));
        Validate addressField           = new Validate((EditText)findViewById(R.id.inputAddress));
        Validate emailField             = new Validate((EditText)findViewById(R.id.inputEmail));
        Validate passwordField          = new Validate((EditText)findViewById(R.id.inputPassword));
        Validate confirmPasswordField   = new Validate((EditText)findViewById(R.id.inputConfirmPassword));
        Validate phoneField             = new Validate((EditText)findViewById(R.id.inputPhone));
        Validate cellPhoneField         = new Validate((EditText)findViewById(R.id.inputCellPhone));

        AbstractValidate phonesFields = new OrTwoRequiredValidate(
                (EditText)findViewById(R.id.inputPhone),
                (EditText)findViewById(R.id.inputCellPhone));
        AbstractValidate confirmFields = new ConfirmValidate(
                (EditText)findViewById(R.id.inputPassword),
                (EditText)findViewById(R.id.inputConfirmPassword));

        Context context = getApplicationContext();

        nameField.addValidator(new NotEmptyValidator(context));
        emailField.addValidator(new NotEmptyValidator(context));
        passwordField.addValidator(new NotEmptyValidator(context));
        confirmPasswordField.addValidator(new NotEmptyValidator(context));

        emailField.addValidator(new EmailValidator(context));

        phoneField.addValidator(new PhoneOrEmptyValidator(context));
        cellPhoneField.addValidator(new PhoneOrEmptyValidator(context));

        registerForm = new Form();
        registerForm.addValidates(nameField);
        registerForm.addValidates(addressField);
        registerForm.addValidates(emailField);
        registerForm.addValidates(passwordField);
        registerForm.addValidates(confirmPasswordField);
        registerForm.addValidates(phoneField);
        registerForm.addValidates(cellPhoneField);
        registerForm.addValidates(phonesFields);
        registerForm.addValidates(confirmFields);
    }

    public void onSendButtonAction(View sender) {
        Log.d("validator", "onSendButtonAction");
        if(registerForm.validate()) {
            Log.d("validator", "is valid");
        } else {
            Log.d("validator", "is not valid");
        }
    }
    
}
