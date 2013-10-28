package phoneticket.android.activities;

import java.io.Serializable;
import java.util.List;

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
import phoneticket.android.activities.fragments.DetailUserShowFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.DiscountFragment;
import phoneticket.android.activities.fragments.RoomFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.interfaces.IArmChairsSelected;

import phoneticket.android.activities.fragments.UserShowsFragment;
import phoneticket.android.activities.interfaces.IDetailUserShowListener;
import phoneticket.android.activities.interfaces.IFunctionSelectionListener;
import phoneticket.android.activities.interfaces.IUserShowsActionListener;
import phoneticket.android.activities.interfaces.IUserShowsListener;
import phoneticket.android.activities.interfaces.IOnCinemaSelectedListener;
import phoneticket.android.activities.interfaces.IOnMovielistItemSelectedListener;
import phoneticket.android.activities.interfaces.IShareActionListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.model.IMyShow;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.Ticket;
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
		IOnMovielistItemSelectedListener, IShareButtonsVisibilityListener,
		IShareActionListener, IFunctionSelectionListener, IArmChairsSelected,
		IUserShowsListener, IDetailUserShowListener, IUserShowsActionListener {

	private RibbonMenuView ribbonMenu;
	private int ribbonMenuItemIdSelected;
	private TextView actionTitle;
	private ImageButton twitterButton;
	private ImageButton facebookButton;
	private ImageButton calendarButton;
	private String twitterMessage;

	private UiLifecycleHelper uiHelper;
	private StatusCallback callback;
	private String facebookUrl;
	private String facebookMessage;
	
	private IOnUserShowChangesListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master);

		UserManager.initialize(getPreferences(0));

		MovieListFragment firstFragment = new MovieListFragment();

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().add(R.id.fragment_container, firstFragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		transaction.commit();

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

			calendarButton = (ImageButton) v.findViewById(R.id.calendarButton);
			calendarButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onCalendarButtonAction();
				}
			});
			hideCalendarButton();

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
		hideCalendarButton();
		MovieListFragment movielistFragment = new MovieListFragment();
		changeFragment(movielistFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_movielist);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_movielist;
	}

	public void changeToCinemasFragment() {
		hideFacebookShareButton();
		hideTwitterShareButton();
		hideCalendarButton();
		CinemasFragment cinemasFragment = new CinemasFragment();
		changeFragment(cinemasFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_cinemas);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_cinemas;
	}

	public void changeToUserFragment() {
		showFacebookShareButton();
		showTwitterShareButton();
		hideCalendarButton();
		twitterMessage = "Soy usuario de CINEMAR, Unite!. Visita www.cinemar.com.ar";
		UserFragment userFragment = new UserFragment();
		changeFragment(userFragment, false);
		if (actionTitle != null)
			actionTitle.setText(R.string.ribbon_menu_user);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_user;
	}

	private void changeToDetailMovieFragment(Bundle movieData) {
		showTwitterShareButton();
		hideCalendarButton();
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
		hideCalendarButton();
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

	private void changeToUserShowsFragment() {
		hideFacebookShareButton();
		hideTwitterShareButton();
		hideCalendarButton();
		UserShowsFragment userShowsFragment = new UserShowsFragment();
		listener = userShowsFragment;
		changeFragment(userShowsFragment, true);
	}

	private void changeToDetailUserShowFragment(Bundle userShowData) {
		hideFacebookShareButton();
		hideTwitterShareButton();
		hideCalendarButton();
		twitterMessage = "Voy a CINEMAR. Visita www.cinemar.com.ar";
		DetailUserShowFragment userShowFragment = new DetailUserShowFragment();
		userShowFragment.setArguments(userShowData);
		changeFragment(userShowFragment, true, DetailUserShowFragment.TAG);
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

	public void changeFragment(Fragment newFragment, boolean addToBackStack,
			String tag) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment, tag);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if (addToBackStack) {
			transaction.addToBackStack(tag);
		}
		transaction.commit();
	}

	protected void onTwitterButtonAction() {
		twitterAction();
	}

	private void twitterAction() {
		String url = "https://twitter.com/intent/tweet?source=webclient&text="
				+ twitterMessage;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	protected void onFacebookButtonAction() {
		if (null != facebookUrl) {
			facebookMovieAction();
		} else {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					this).setApplicationName("PhoneTicket")
					.setDescription(this.facebookMessage).build();
			if (uiHelper != null)
				uiHelper.trackPendingDialogCall(shareDialog.present());
		}
	}

	protected void onCalendarButtonAction() {
		// TODO
	}

	private void facebookMovieAction() {
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
				.setLink(facebookUrl).setApplicationName("PhoneTicket")
				.setDescription("Vamos a ver esta película a Cinemar").build();
		if (uiHelper != null)
			uiHelper.trackPendingDialogCall(shareDialog.present());
	}

	private void facebookCinemaAction(double latitude, double longitude,
			String address, String cinemaName) {
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
				.setLink(
						"http://maps.google.com/maps?q=" + latitude + ","
								+ longitude).setApplicationName("PhoneTicket")
				.setName("Vamos al complejo " + cinemaName + " de Cinemar")
				.setDescription(address).build();
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
	public void onFunctionSelected(Ticket ticket) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(RoomFragment.TICKET_INFO, ticket);
		changeToRoomFragment(bundle);
	}

	@Override
	public void onShowUserShowsAction() {
		changeToUserShowsFragment();
	}

	@Override
	public void onShowDetailUserShowAction(IMyShow userShow) {
		Bundle bundle = new Bundle();
		bundle.putInt(DetailUserShowFragment.USER_SHOW_INFO, userShow.getId());
		changeToDetailUserShowFragment(bundle);
	}

	private void changeToRoomFragment(Bundle bundle) {
		hideFacebookShareButton();
		hideTwitterShareButton();
		RoomFragment roomFragment = new RoomFragment();
		roomFragment.setArguments(bundle);
		changeFragment(roomFragment, true);
	}

	@Override
	public void onArmChairsSelected(List<ArmChair> armChairs, Ticket ticket) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(DiscountFragment.ARM_CHAIRS_SELECTED,
				(Serializable) armChairs);
		bundle.putSerializable(DiscountFragment.TICKET, (Serializable) ticket);
		changeToDiscountFragment(bundle);
	}

	private void changeToDiscountFragment(Bundle bundle) {
		hideFacebookShareButton();
		hideTwitterShareButton();
		DiscountFragment r = new DiscountFragment();
		r.setArguments(bundle);
		changeFragment(r, true);

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
	public void hideCalendarButton() {
		if (null != calendarButton)
			calendarButton.setVisibility(ImageButton.GONE);
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

	@Override
	public void showCalendarButton() {
		if (null != calendarButton)
			calendarButton.setVisibility(ImageButton.VISIBLE);
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

	@Override
	public void shareOnTwitter(String twitterMessage) {
		this.twitterMessage = twitterMessage;
		twitterAction();
	}

	@Override
	public void shareMovieOnFacebook(String url) {
		this.facebookUrl = url;
	}

	@Override
	public void shareCinemaOnFacebook(double latitude, double longitude,
			String address, String cinemaName) {
		facebookCinemaAction(latitude, longitude, address, cinemaName);
	}

	@Override
	public void shareTextonFacebook(String message) {
		this.facebookMessage = message;
		this.facebookUrl = null;
	}

	@Override
	public void onCanceledUserShowAction(IDetailUserShow userShow) {
		onBackPressed();
		listener.userShowCanceled(userShow);
	}
	
	public interface IOnUserShowChangesListener {

		void userShowCanceled(IDetailUserShow userShow);
		
	}
}
