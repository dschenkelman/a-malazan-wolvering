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
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveCinemaInfoService;

import android.content.Intent;
import android.widget.Button;

@RunWith(RobolectricTestRunner.class)
public class DetailCinemaTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;

	@Mock
	private IRetrieveCinemaInfoService cinemaInfoService;

	private DetailCinemaFragment fragment;
	private MasterActivity activity;
	private int cinemaId;
	private Button googleMapButton;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveCinemaInfoService.class, cinemaInfoService);

		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();
		cinemaId = 0;
		activity.onCinemaSelected(cinemaId, "Cine");

		fragment = (DetailCinemaFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

		googleMapButton = (Button) fragment.getView().findViewById(
				R.id.showInGoogleMaps);

	}

	@Test
	public void detailCinemaActivityCallRetrieveCinemaInfoServiceOnCreate() {
		Mockito.verify(cinemaInfoService, Mockito.times(1)).retrieveCinemaInfo(
				fragment, cinemaId);
	}

	@Test
	public void actionViewIntentOnGoogleMapsButtonPress() {
		new MockRetrieveCinemaInfoService().retrieveCinemaInfo(fragment,
				cinemaId);
		googleMapButton.performClick();
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		assertNotNull(intent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
		assertThat(shadowIntent.getAction(), equalTo(Intent.ACTION_VIEW));
	}

}
