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
import org.robolectric.shadows.ShadowPreferenceManager;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DetailUserShowFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.fragments.UserShowsFragment;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.mock.MockRetrieveMyShowsService;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.utils.UserManager;

@RunWith(RobolectricTestRunner.class)
public class UserShowsTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;
	@Mock
	private IRetrieveUserInfoService iRetrieveUserInfoService;
	@Mock
	private IRetrieveMyShowsService iRetrieveMyShowsService;
	@Mock
	private IRetrieveUserShowInfoService iRetrieveUserShowInfoService;
	@Mock
	private ICancelUserShowService cancelService;
	@Mock
	private IConfirmReservationService confirmService;

	private UserShowsFragment userShowsFragment;
	private MasterActivity activity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);
		module.addBinding(IRetrieveUserInfoService.class,
				iRetrieveUserInfoService);
		module.addBinding(IRetrieveMyShowsService.class,
				iRetrieveMyShowsService);
		module.addBinding(IRetrieveUserShowInfoService.class,
				iRetrieveUserShowInfoService);
		module.addBinding(ICancelUserShowService.class,
				cancelService);
		module.addBinding(IConfirmReservationService.class,
				confirmService);

		TestModule.setUp(this, module);

		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);

		UserManager.getInstance().setCredentials("mati@gmail.com", "password");

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

		UserManager.getInstance().setCredentials("mati@gmail.com", "password");
		UserManager.getInstance().loginUserWithId(0, true);

		activity.changeToUserFragment();

		UserFragment userFragment = (UserFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		Button button = (Button) userFragment.getView().findViewById(
				R.id.buyedAndReservedFunctionsButton);
		button.performClick();

		userShowsFragment = (UserShowsFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);
	}

	@Test
	public void retrieveMyShowsShouldBeCalledOnceOnCreate() {
		Mockito.verify(iRetrieveMyShowsService, Mockito.times(1))
				.retrieveMyShows(userShowsFragment);
	}
	
	@Test
	public void myShowsListShouldHaveSomeChildsWhenShowsAreRetrieved() {
		new MockRetrieveMyShowsService().retrieveMyShows(userShowsFragment);
		
		ListView showsList = (ListView) userShowsFragment.getView()
				.findViewById(R.id.myShowsListView);
		
		Assert.assertTrue(0 != showsList.getAdapter().getCount());
	}

	@Test
	public void userShowsFragmentChangeToDetailUserShowFragmentOnItemClick() {
		new MockRetrieveMyShowsService().retrieveMyShows(userShowsFragment);
		
		ListView showsList = (ListView) userShowsFragment.getView()
				.findViewById(R.id.myShowsListView);
		
		showsList.getOnItemClickListener().onItemClick(null, null, 0, 0);

		Assert.assertEquals(
				DetailUserShowFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}
}
