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
import phoneticket.android.activities.fragments.CinemasFragment;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveCinemaListService;
import android.widget.ListView;

@RunWith(RobolectricTestRunner.class)
public class MovieCinemaTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;

	@Mock
	private IRetrieveCinemaListService cinemaListService;

	@Mock
	private IRetrieveCinemaInfoService cinemaInfoService;

	private MasterActivity activity;
	private CinemasFragment fragment;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);
		module.addBinding(IRetrieveCinemaListService.class, cinemaListService);
		module.addBinding(IRetrieveCinemaInfoService.class, cinemaInfoService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

		activity.changeToCinemasFragment();
		fragment = (CinemasFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

	}

	@Test
	public void detailMovieActivityCallRetrieveMovieListServiceOnCreate() {
		Mockito.verify(cinemaListService, Mockito.times(1)).retrieveCinemaList(
				fragment);
	}

	@Test
	public void detailMovieChangeFragmentOnItemClick() {
		ListView cinemaListView = (ListView) fragment.getView().findViewById(
				R.id.cinemaList);

		new MockRetrieveCinemaListService().retrieveCinemaList(fragment);

		cinemaListView.getOnItemClickListener().onItemClick(null, null, 0, 0);

		Assert.assertEquals(
				DetailCinemaFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());

	}

	@Test
	public void movieListActivityCallRetrieveMovieListServiceOnRefreshMovieListAction() {
		fragment.onRefreshCinemasListAction();
		Mockito.verify(this.cinemaListService, Mockito.times(2))
				.retrieveCinemaList(fragment);
	}

}
