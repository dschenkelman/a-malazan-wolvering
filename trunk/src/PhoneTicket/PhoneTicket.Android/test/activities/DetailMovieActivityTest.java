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

import phoneticket.android.R;
import phoneticket.android.activities.DetailMovieActivity;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.mock.MockRetrieveMovieInfoService;

import android.content.Intent;
import android.widget.Button;

@RunWith(RobolectricTestRunner.class)
public class DetailMovieActivityTest {

	@Mock
	private IRetrieveMovieInfoService retrieveMovieInfoService;
	private DetailMovieActivity activity;
	private int movieId;
	private Button watchTrailerButton;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();
		module.addBinding(IRetrieveMovieInfoService.class,
				retrieveMovieInfoService);
		TestModule.setUp(this, module);
		movieId = 0;
		Intent intent = new Intent(Robolectric.getShadowApplication()
				.getApplicationContext(), DetailMovieActivity.class);
		intent.putExtra(DetailMovieActivity.MovieToShowId, movieId);
		activity = Robolectric.buildActivity(DetailMovieActivity.class)
				.withIntent(intent).create().get();

		watchTrailerButton = (Button) activity
				.findViewById(R.id.watchTrailerButton);
	}

	@Test
	public void detailMovieActivityCallRetrieveMovieInfoServiceOnCreate() {
		Mockito.verify(retrieveMovieInfoService, Mockito.times(1))
				.retrieveMovieInfo(activity, movieId);
	}

	@Test
	public void movieListActivityCallRetrieveMovieListServiceOnCreate() {
		new MockRetrieveMovieInfoService().retrieveMovieInfo(activity, 0);
		watchTrailerButton.performClick();
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		assertNotNull(intent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
		assertThat(shadowIntent.getAction(), equalTo(Intent.ACTION_VIEW));
	}

}
