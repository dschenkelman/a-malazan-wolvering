package phoneticket.android.activities;

import com.throrinstudio.android.common.libs.validator.Form;

import phoneticket.android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class RegisterUserActivity extends Activity {

	private Form form;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		createForm();
	}
	
	private void createForm() {
		form = new Form();
	}

	public void onRegisterButtonAction(View sender) {
		
	}
}
