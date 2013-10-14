package activities;

import junit.framework.Assert;
import module.TestModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.CinemasFragment;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;

@RunWith(RobolectricTestRunner.class)
public class MasterActivityTest {

	@Mock
	private IRetrieveCinemaListService cinemaListService;

	@Mock
	private IRetrieveCinemaInfoService cinemaInfoService;

	@Mock
	private IRetrieveMovieInfoService movieInfoService;

	@Mock
	private IRetrieveMovieFunctionsService movieFunctionsService;

	@Mock
	private IRetrieveMovieListService movieListService;

	private MasterActivity activity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveCinemaListService.class, cinemaListService);
		module.addBinding(IRetrieveCinemaInfoService.class, cinemaInfoService);
		module.addBinding(IRetrieveMovieInfoService.class, movieInfoService);
		module.addBinding(IRetrieveMovieFunctionsService.class,
				movieFunctionsService);
		module.addBinding(IRetrieveMovieListService.class, movieListService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

	}

	@Test
	public void changeToCinemasFragment() {
		activity.changeToCinemasFragment();
		Assert.assertEquals(
				CinemasFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToMovieListFragment() {
		activity.changeToMovieListFragment();
		Assert.assertEquals(
				MovieListFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToUserFragment() {
		activity.changeToUserFragment();
		Assert.assertEquals(
				UserFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToDetailMovieFragment() {
		activity.onMovielistItemSelected(0, "");
		Assert.assertEquals(
				DetailMovieFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToDetailCinemaFragment() {
		activity.onCinemaSelected(0, "");
		Assert.assertEquals(
				DetailCinemaFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

}
