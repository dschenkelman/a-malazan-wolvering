package activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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

import com.origamilabs.library.views.StaggeredGridView;

import phoneticket.android.R;
import phoneticket.android.activities.DetailMovieActivity;
import phoneticket.android.activities.MovieListActivity;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveMovieListService;
import android.content.Intent;

@RunWith(RobolectricTestRunner.class)
public class MovieListActivityTest {

	@Mock
	private IRetrieveMovieListService retrieveMovieListService;
	private StaggeredGridView view;
	private MovieListActivity activity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();
		module.addBinding(IRetrieveMovieListService.class,
				retrieveMovieListService);
		TestModule.setUp(this, module);
		activity = Robolectric.buildActivity(MovieListActivity.class).create()
				.get();
		view = (StaggeredGridView) activity
				.findViewById(R.id.movieListContainer);
	}

	@Test
	public void movieListActivityCallRetrieveMovieListServiceOnCreate() {
		Mockito.verify(retrieveMovieListService, Mockito.times(1))
				.retrieveMovieList(activity);
	}

	@Test
	public void movieListActivityCallRetrieveMovieListServiceOnRefreshMovieListAction() {
		activity.onRefreshMovieListAction(null);
		Mockito.verify(retrieveMovieListService, Mockito.times(2))
				.retrieveMovieList(activity);
	}

	@Test
	public void movieListActivityCallDetailMovieActivityOnClick() {
		new MockRetrieveMovieListService().retrieveMovieList(activity);
		view.getOnItemClickListener().onItemClick(view, view, 0, 0);

		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		assertNotNull(intent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
		assertThat(shadowIntent.getComponent().getClassName(),
				equalTo(DetailMovieActivity.class.getName()));
	}
}
