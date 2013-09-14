package phoneticket.android.activities;

import phoneticket.android.R;
import phoneticket.android.utils.UserManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

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

}
