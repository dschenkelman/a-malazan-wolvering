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

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveMovieFunctionsService;
import phoneticket.android.services.get.mock.MockRetrieveMovieInfoService;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

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

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();
		movieId = 0;
		activity.onMovielistItemSelected(movieId, "Titulo");

		fragment = (DetailMovieFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

		watchTrailerButton = (Button) fragment.getView().findViewById(
				R.id.watchTrailerButton);
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

}
