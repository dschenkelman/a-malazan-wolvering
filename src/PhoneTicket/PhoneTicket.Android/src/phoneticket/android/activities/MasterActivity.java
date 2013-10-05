package phoneticket.android.activities;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import phoneticket.android.R;
import phoneticket.android.activities.fragments.CinemasFragment;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.MovieListFragment.IOnMovieSelectedListener;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.utils.UserManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MasterActivity extends FragmentActivity implements
		iRibbonMenuCallback, IOnMovieSelectedListener {

	private RibbonMenuView ribbonMenu;
	private int ribbonMenuItemIdSelected;
	private TextView actionTitle;

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
		MovieListFragment movielistFragment = new MovieListFragment();
		changeFragment(R.id.fragment_container, movielistFragment);
		actionTitle.setText(R.string.ribbon_menu_movielist);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_movielist;
	}

	private void changeToCinemasFragment() {
		CinemasFragment cinemasFragment = new CinemasFragment();
		changeFragment(R.id.fragment_container, cinemasFragment);
		actionTitle.setText(R.string.ribbon_menu_cinemas);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_cinemas;
	}

	private void changeToUserFragment() {
		UserFragment userFragment = new UserFragment();
		changeFragment(R.id.fragment_container, userFragment);
		actionTitle.setText(R.string.ribbon_menu_user);
		ribbonMenuItemIdSelected = R.id.ribbon_menu_user;
	}

	private void changeToDetailMovieFragment(Bundle movieData) {
		DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
		detailMovieFragment.setArguments(movieData);
		changeFragment(R.id.fragment_container, detailMovieFragment);
	}

	private void changeFragment(int containerId, Fragment newFragment) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(containerId, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onSelectedSelected(int movieId) {
		Bundle movieData = new Bundle();
		movieData.putInt(DetailMovieFragment.EXTRA_MOVIE_ID, movieId);
		changeToDetailMovieFragment(movieData);
	}
}
