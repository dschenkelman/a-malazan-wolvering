package activities;

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

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveMovieListService;
import android.widget.GridView;

@RunWith(RobolectricTestRunner.class)
public class MovieListTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;

	@Mock
	private IRetrieveMovieInfoService retrieveMovieInfoService;
	@Mock
	private IRetrieveMovieFunctionsService movieFunctionsService;

	private MasterActivity activity;
	private MovieListFragment fragment;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);
		module.addBinding(IRetrieveMovieInfoService.class,
				retrieveMovieInfoService);
		module.addBinding(IRetrieveMovieFunctionsService.class,
				movieFunctionsService);
		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

		fragment = (MovieListFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

	}

	@Test
	public void detailMovieActivityCallRetrieveMovieListServiceOnCreate() {
		Mockito.verify(iRetrieveMovieListService, Mockito.times(1))
				.retrieveMovieList(fragment);
	}

	@Test
	public void detailMovieChangeFragmentOnItemClick() {
		GridView gridView = (GridView) fragment.getView().findViewById(
				R.id.gridview);
		new MockRetrieveMovieListService().retrieveMovieList(fragment);

		gridView.getOnItemClickListener().onItemClick(null, null, 0, 0);

		Assert.assertEquals(
				DetailMovieFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());

	}

	@Test
	public void movieListActivityCallRetrieveMovieListServiceOnRefreshMovieListAction() {
		fragment.onRefreshMovieListAction();
		Mockito.verify(iRetrieveMovieListService, Mockito.times(2))
				.retrieveMovieList(fragment);
	}

}
