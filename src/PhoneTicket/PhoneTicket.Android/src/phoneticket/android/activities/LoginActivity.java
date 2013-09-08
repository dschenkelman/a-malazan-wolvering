package phoneticket.android.activities;

import phoneticket.android.R;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void onLoginButtonAction(View sender) {
		
	}
	
	public void onRegisterButtonAction(View sender) {
		Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
	}
}
