package phoneticket.android.activities;

import phoneticket.android.R;
import phoneticket.android.model.User;
import phoneticket.android.utils.UserManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UserManager.initialize(getPreferences(0));
	}

	public void onActionButtonTapped(View sender) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		User user = UserManager.getInstance().getLogedUser();
		if (user != null)
			((TextView) findViewById(R.id.loginUser)).setText(user.toString());
		else
			((TextView) findViewById(R.id.loginUser)).setText("");
	}

}
