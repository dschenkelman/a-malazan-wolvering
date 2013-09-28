package phoneticket.android.activities;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.model.User;
import phoneticket.android.utils.UserManager;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		IMessageDialogDataSource, iRibbonMenuCallback {

	private RibbonMenuView ribbonMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UserManager.initialize(getPreferences(0));

		updateLogoutButtonVisibility();
		createRibbonMenu();
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// getActionBar().setIcon(R.drawable.ic_slider_menu);

			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.custom_action_bar, null);
			ImageButton menuButton = (ImageButton) v.findViewById(R.id.ribbonMenuButton);
			menuButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ribbonMenu.toggleMenu();
				}
			});

			actionBar.setCustomView(v);

		}
	}

	private void createRibbonMenu() {
		ribbonMenu = (RibbonMenuView) findViewById(R.id.ribbonMenu);
		ribbonMenu.setMenuClickCallback(this);
		ribbonMenu.setMenuItems(R.menu.ribbon_menu);
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

	public void onMovieListButtonTapped(View sender) {
		Intent intent = new Intent(this, MovieListActivity.class);
		startActivity(intent);
	}

	public void onMockMovieButtonTapped(View sender) {
		int mockMovieID = 1;
		Intent intent = new Intent(this, DetailMovieActivity.class);
		intent.putExtra(DetailMovieActivity.MovieToShowId, mockMovieID);
		startActivity(intent);
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

	@Override
	public void RibbonMenuItemClick(int itemId) {
		// TODO Auto-generated method stub

	}

}
