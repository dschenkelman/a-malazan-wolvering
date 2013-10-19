package phoneticket.android.activities;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;
import com.facebook.Session.StatusCallback;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import phoneticket.android.R;
import phoneticket.android.activities.fragments.CinemasFragment;

import phoneticket.android.activities.fragments.DetailCinemaFragment;

import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.interfaces.IOnCinemaSelectedListener;
import phoneticket.android.activities.interfaces.IOnMovielistItemSelectedListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.utils.UserManager;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MasterActivity extends RoboFragmentActivity implements
		iRibbonMenuCallback, IOnCinemaSelectedListener,
		IOnMovielistItemSelectedListener, IShareButtonsVisibilityListener {

	private RibbonMenuView ribbonMenu;
	private int ribbonMenuItemIdSelected;
	private TextView actionTitle;
	private ImageButton twitterButton;
	private ImageButton facebookButton;
	private String twitterMessage;

	private UiLifecycleHelper uiHelper;
	private StatusCallback callback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master);

		UserManager.initialize(getPreferences(0));

		MovieListFragment firstFragment = new MovieListFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, firstFragment).commit();
		ribbonMenuItemIdSelected = R.id.ribbon_menu_movielist;

		setupActionBar();
		createRibbonMenu();

		if (containsApplicationIdOfFacebook()) {
			uiHelper = new UiLifecycleHelper(this, callback);
			uiHelper.onCreate(savedInstanceState);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (uiHelper != null)
			uiHelper.onActivityResult(requestCode, resultCode, data,
					new FacebookDialog.Callback() {
						@Override
						public void onError(
								FacebookDialog.PendingCall pendingCall,
								Exception error, Bundle data) {
							Log.e("Activity", String.format("Error: %s",
									error.toString()));
						}

						@Override
						public void onComplete(
								FacebookDialog.PendingCall pendingCall,
								Bundle data) {
							Log.i("Activity", "Success!");
						}
					});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (uiHelper != null)
			uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (uiHelper != null)
			uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (uiHelper != null)
			uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (uiHelper != null)
			uiHelper.onDestroy();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
				&& actionBar != null) {

			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.custom_action_bar, null);
			ImageButton menuButton = (ImageButton) v
					.findViewById(R.id.ribbonMenuButton);
			actionTitle = (TextView) v.findViewById(R.id.actionTitle);
			menuButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ribbonMenu.toggleMenu();
				}
			});

			twitterButton = (ImageButton) v.findViewById(R.id.twitterButton);
			twitterButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onTwitterButtonAction();
				}
			});
			hideTwitterShareButton();

			facebookButton = (ImageButton) v.findViewById(R.id.facebookButton);
			facebookButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onFacebookButtonAction();
				}
			});
			hideFacebookShareButton();

			actionBar.setCustomView(v);
		}
	}

	private void createRibbonMenu() {
		ribbonMenu = (RibbonMenuView) findViewById(R.id.ribbonMenu);
		ribbonMenu.setMenuClickCallback(this);
		ribbonMenu.setMenuItems(R.menu.ribbon_menu);
	}

	@Override
	public void RibbonMenuItemClick(int itemId) {
		Log.d("PhoneTicket", "RibbonMenuItemId: " + itemId);
		if (ribbonMenuItemIdSelected != itemId) {
			switch (itemId) {
			case R.id.ribbon_menu_movielist: {
				changeToMovieListFragment();
				break;
			}
			case R.id.ribbon_menu_cinemas: {
				changeToCinemasFragment();
				break;
			}
			case R.id.ribbon_menu_user: {
				changeToUserFragment();
				break;
			}
			}
		}
	}

	public void changeToMovieListFragment() {
		hideFacebookShareButton();
		hideTwitterShareButton();
		MovieListFragment movielistFragment = new MovieListFragment();
		changeFragment(movielistFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_movielist);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_movielist;
	}

	public void changeToCinemasFragment() {
		hideFacebookShareButton();
		hideTwitterShareButton();
		CinemasFragment cinemasFragment = new CinemasFragment();
		changeFragment(cinemasFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_cinemas);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_cinemas;
	}

	public void changeToUserFragment() {
		showFacebookShareButton();
		showTwitterShareButton();
		twitterMessage = "Soy usuario de CINEMAR, Unite!. Visita www.cinemar.com.ar";
		UserFragment userFragment = new UserFragment();
		changeFragment(userFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_user);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_user;
	}

	private void changeToDetailMovieFragment(Bundle movieData) {
		showFacebookShareButton();
		showTwitterShareButton();
		twitterMessage = "Voy a mirar una película CINEMAR. Visita www.cinemar.com.ar";
		String movieName = movieData
				.getString(DetailMovieFragment.EXTRA_MOVIE_TITLE);
		if (null != movieName) {
			if (0 < movieName.length()) {
				twitterMessage = "Voy a ver " + movieName
						+ " a CINEMAR. Visita www.cinemar.com.ar";
			}
		}
		DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
		detailMovieFragment.setArguments(movieData);
		changeFragment(detailMovieFragment, true);
	}

	private void changeToDetailCinemaFragment(Bundle cinemaData) {
		showFacebookShareButton();
		showTwitterShareButton();
		twitterMessage = "Voy a CINEMAR. Visita www.cinemar.com.ar";
		String name = cinemaData
				.getString(DetailCinemaFragment.EXTRA_CINEMA_NAME);
		if (null != name) {
			if (0 < name.length()) {
				twitterMessage = "Voy al complejo " + name
						+ " de CINEMAR. Visita www.cinemar.com.ar";
			}
		}
		DetailCinemaFragment detailCinemaFragment = new DetailCinemaFragment();
		detailCinemaFragment.setArguments(cinemaData);
		changeFragment(detailCinemaFragment, true);
	}

	public void changeFragment(Fragment newFragment, boolean addToBackStack) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	protected void onTwitterButtonAction() {
		String url = "https://twitter.com/intent/tweet?source=webclient&text="
				+ twitterMessage;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	protected void onFacebookButtonAction() {
		Log.d("PhoneTicket", "Share on facebook");
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
				.setLink("https://developers.facebook.com/android").build();
		if (uiHelper != null)
			uiHelper.trackPendingDialogCall(shareDialog.present());
	}

	@Override
	public void onCinemaSelected(int cinemaId, String cinemaName) {
		Bundle cinemaData = new Bundle();
		cinemaData.putInt(DetailCinemaFragment.EXTRA_CINEMA_ID, cinemaId);
		cinemaData
				.putString(DetailCinemaFragment.EXTRA_CINEMA_NAME, cinemaName);
		changeToDetailCinemaFragment(cinemaData);
	}

	@Override
	public void onMovielistItemSelected(int movieId, String movieTitle) {
		Bundle movieData = new Bundle();
		movieData.putInt(DetailMovieFragment.EXTRA_MOVIE_ID, movieId);
		movieData.putString(DetailMovieFragment.EXTRA_MOVIE_TITLE, movieTitle);
		changeToDetailMovieFragment(movieData);
	}

	@Override
	public void hideFacebookShareButton() {
		if (null != facebookButton)
			facebookButton.setVisibility(ImageButton.GONE);
	}

	@Override
	public void hideTwitterShareButton() {
		if (null != twitterButton)
			twitterButton.setVisibility(ImageButton.GONE);
	}

	@Override
	public void showFacebookShareButton() {
		if (null != facebookButton)
			facebookButton.setVisibility(ImageButton.VISIBLE);
	}

	@Override
	public void showTwitterShareButton() {
		if (null != twitterButton)
			twitterButton.setVisibility(ImageButton.VISIBLE);
	}

	private boolean containsApplicationIdOfFacebook() {
		ApplicationInfo ai;
		try {
			ai = this.getPackageManager().getApplicationInfo(
					this.getPackageName(), PackageManager.GET_META_DATA);
			return ai.metaData != null
					&& ai.metaData.getString(Session.APPLICATION_ID_PROPERTY) != null;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}
