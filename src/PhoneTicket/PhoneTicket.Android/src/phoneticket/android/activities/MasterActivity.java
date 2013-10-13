package phoneticket.android.activities;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import phoneticket.android.R;
import phoneticket.android.activities.fragments.CinemasFragment;

import phoneticket.android.activities.fragments.DetailCinemaFragment;

import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.interfaces.IOnCinemaSelectedListener;
import phoneticket.android.activities.interfaces.IOnMovielistItemSelectedListener;
import phoneticket.android.utils.UserManager;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
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
		IOnMovielistItemSelectedListener {

	private RibbonMenuView ribbonMenu;
	private int ribbonMenuItemIdSelected;
	private TextView actionTitle;
	private ImageButton twitterButton;
	private String twitterMessage;

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
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
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
			twitterButton.setVisibility(ImageButton.GONE);

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

	private void changeToMovieListFragment() {
		twitterButton.setVisibility(ImageButton.GONE);
		MovieListFragment movielistFragment = new MovieListFragment();
		changeFragment(movielistFragment, false);
		actionTitle.setText(R.string.ribbon_menu_movielist);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_movielist;
	}

	private void changeToCinemasFragment() {
		twitterButton.setVisibility(ImageButton.GONE);
		CinemasFragment cinemasFragment = new CinemasFragment();
		changeFragment(cinemasFragment, false);
		actionTitle.setText(R.string.ribbon_menu_cinemas);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_cinemas;
	}

	private void changeToUserFragment() {
		twitterButton.setVisibility(ImageButton.VISIBLE);
		twitterMessage = "Soy usuario de CINEMAR, Unite!. Visita www.cinemar.com.ar";
		UserFragment userFragment = new UserFragment();
		changeFragment(userFragment, false);
		actionTitle.setText(R.string.ribbon_menu_user);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_user;
	}

	private void changeToDetailMovieFragment(Bundle movieData) {
		twitterButton.setVisibility(ImageButton.VISIBLE);
		String movieName = movieData
				.getString(DetailMovieFragment.EXTRA_MOVIE_TITLE);
		twitterMessage = "Voy a ver " + movieName
				+ " a CINEMAR. Visita www.cinemar.com.ar";
		DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
		detailMovieFragment.setArguments(movieData);
		changeFragment(detailMovieFragment, true);
	}

	private void changeToDetailCinemaFragment(Bundle cinemaData) {
		twitterButton.setVisibility(ImageButton.VISIBLE);
		twitterMessage = "Voy a CINEMAR. Visita www.cinemar.com.ar";
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

	@Override
	public void onCinemaSelected(int cinemaId) {
		Bundle cinemaData = new Bundle();
		cinemaData.putInt(DetailCinemaFragment.EXTRA_CINEMA_ID, cinemaId);
		changeToDetailCinemaFragment(cinemaData);
	}

	@Override
	public void onMovielistItemSelected(int movieId, String movieTitle) {
		Bundle movieData = new Bundle();
		movieData.putInt(DetailMovieFragment.EXTRA_MOVIE_ID, movieId);
		movieData.putString(DetailMovieFragment.EXTRA_MOVIE_TITLE, movieTitle);
		changeToDetailMovieFragment(movieData);
	}
}
