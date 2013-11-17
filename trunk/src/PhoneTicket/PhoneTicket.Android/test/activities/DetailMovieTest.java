package activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;
import module.TestModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowPreferenceManager;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.RoomFragment;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.mock.MockRetrieveMovieFunctionsService;
import phoneticket.android.services.get.mock.MockRetrieveMovieInfoService;
import phoneticket.android.utils.UserManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@RunWith(RobolectricTestRunner.class)
public class DetailMovieTest {

	@Mock
	private IRetrieveMovieInfoService retrieveMovieInfoService;
	@Mock
	private IRetrieveMovieFunctionsService movieFunctionsService;
	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;
	@Mock
	private IRetrieveCinemaInfoService iRetrieveCinemaInfoService;
	@Mock
	private IRetrieveRoomInfoService roomInfoService;

	private DetailMovieFragment fragment;
	private MasterActivity activity;
	private int movieId;
	private Button watchTrailerButton;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveMovieInfoService.class,
				retrieveMovieInfoService);
		module.addBinding(IRetrieveMovieFunctionsService.class,
				movieFunctionsService);
		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);
		module.addBinding(IRetrieveCinemaInfoService.class,
				iRetrieveCinemaInfoService);
		module.addBinding(IRetrieveRoomInfoService.class, roomInfoService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();
		movieId = 0;
		activity.onMovielistItemSelected(movieId, "Titulo");

		fragment = (DetailMovieFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

		watchTrailerButton = (Button) fragment.getView().findViewById(
				R.id.watchTrailerButton);
		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);
	}

	@Test
	public void detailMovieActivityCallRetrieveMovieFunctionsServiceOnCreate() {
		Mockito.verify(movieFunctionsService, Mockito.times(1))
				.retrieveMovieFunctions(fragment, movieId);
	}

	@Test
	public void detailMovieActivityCallRetrieveMovieInfoServiceOnCreate() {
		Mockito.verify(retrieveMovieInfoService, Mockito.times(1))
				.retrieveMovieInfo(fragment, movieId);
	}

	@Test
	public void actionViewIntentOnWatchTrailer() {
		new MockRetrieveMovieInfoService().retrieveMovieInfo(fragment, 0);
		watchTrailerButton.performClick();
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		assertNotNull(intent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
		assertThat(shadowIntent.getAction(), equalTo(Intent.ACTION_VIEW));
	}

	@Test
	public void detailMovieChangeToDetailCinemaFragmentOnItemClick() {
		new MockRetrieveMovieFunctionsService().retrieveMovieFunctions(
				fragment, movieId);

		ImageButton button = (ImageButton) fragment.getView().findViewById(
				R.id.goToCinema);

		button.performClick();

		Assert.assertEquals(
				DetailCinemaFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void detailMovieChangeCallLoginActivityWhenNotLogIn() {
		new MockRetrieveMovieInfoService().retrieveMovieInfo(fragment, 0);

		new MockRetrieveMovieFunctionsService().retrieveMovieFunctions(
				fragment, movieId);

		LinearLayout button = (LinearLayout) fragment.getView().findViewById(
				R.id.timeLinearLayout);

		TextView v = (TextView) button.getChildAt(0);

		v.performClick();

		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		ComponentName c = intent.getComponent();
		String call = c.getClassName();
		assertNotNull(intent);
		Assert.assertEquals(LoginActivity.class.getName(), call);
	}

	@Test
	public void detailMovieChangeChangeFragmentOnLoginIn() {
		new MockRetrieveMovieInfoService().retrieveMovieInfo(fragment, 0);

		new MockRetrieveMovieFunctionsService().retrieveMovieFunctions(
				fragment, movieId);

		LinearLayout button = (LinearLayout) fragment.getView().findViewById(
				R.id.timeLinearLayout);

		UserManager.getInstance().setCredentials("pepe", "pepe");
		UserManager.getInstance().loginUserWithId(0, true);
		TextView v = (TextView) button.getChildAt(0);

		v.performClick();

		Assert.assertEquals(
				RoomFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

}
