package phoneticket.android.activities;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.model.User;
import phoneticket.android.utils.UserManager;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		IMessageDialogDataSource {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UserManager.initialize(getPreferences(0));

		updateLogoutButtonVisibility();
	}

	private void updateLogoutButtonVisibility() {
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		int visibility = UserManager.getInstance().isUserLoged() ? Button.VISIBLE
				: Button.GONE;
		logoutButton.setVisibility(visibility);
	}

	public void onActionButtonTapped(View sender) {
		if (UserManager.getInstance().isUserLoged()) {
			MessageDialogFragment dialog = new MessageDialogFragment();
			dialog.show(getSupportFragmentManager(), "dialog.actionmessage");
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	public void onLogoutButtonTapped(View sender) {
		UserManager.getInstance().logoutUser();
		updateLogoutButtonVisibility();
		((TextView) findViewById(R.id.loginUser)).setText("");
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateLogoutButtonVisibility();
		User user = UserManager.getInstance().getLogedUser();
		if (UserManager.getInstance().isUserLoged())
			((TextView) findViewById(R.id.loginUser)).setText(user.toString());
		else
			((TextView) findViewById(R.id.loginUser)).setText("");
	}

	@Override
	public String getMessage() {
		return "Realizada con exito";
	}

	@Override
	public String getMessageTitle() {
		return "Acción";
	}

}
