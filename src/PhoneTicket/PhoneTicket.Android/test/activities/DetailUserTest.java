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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowPreferenceManager;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.fragments.UserShowsFragment;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.utils.UserManager;

@RunWith(RobolectricTestRunner.class)
public class DetailUserTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;
	@Mock
	private IRetrieveUserInfoService iRetrieveUserInfoService;
	@Mock
	private IRetrieveMyShowsService iRetrieveMyShowsService;

	private UserFragment fragmentNotLogedUser;
	private MasterActivity activityNotLogedUser;

	private UserFragment fragmentLogedUser;
	private MasterActivity activityLogedUser;

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

		TestModule.setUp(this, module);

		activityNotLogedUser = Robolectric.buildActivity(MasterActivity.class)
				.create().resume().get();

		activityNotLogedUser.changeToUserFragment();

		fragmentNotLogedUser = (UserFragment) activityNotLogedUser
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);

		UserManager.getInstance().setCredentials("mati@gmail.com", "password");

		activityLogedUser = Robolectric.buildActivity(MasterActivity.class)
				.create().resume().get();

		UserManager.getInstance().setCredentials("mati@gmail.com", "password");
		UserManager.getInstance().loginUserWithId(0, true);

		activityLogedUser.changeToUserFragment();

		fragmentLogedUser = (UserFragment) activityLogedUser
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

	}

	@Test
	public void noSessionViewShouldBeVisibleIfNoUserIsLoged() {
		LinearLayout noSessionView = (LinearLayout) fragmentNotLogedUser
				.getView().findViewById(R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) fragmentNotLogedUser.getView()
				.findViewById(R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) fragmentNotLogedUser
				.getView().findViewById(R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) fragmentNotLogedUser.getView()
				.findViewById(R.id.userView);

		Assert.assertEquals(LinearLayout.VISIBLE, noSessionView.getVisibility());
		Assert.assertEquals(LinearLayout.GONE, errorView.getVisibility());
		Assert.assertEquals(RelativeLayout.GONE, loadingView.getVisibility());
		Assert.assertEquals(ScrollView.GONE, userView.getVisibility());
	}

	@Test
	public void noSessionViewShouldNotBeVisibleIfUserIsLoged() {
		LinearLayout noSessionView = (LinearLayout) fragmentLogedUser.getView()
				.findViewById(R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) fragmentLogedUser.getView()
				.findViewById(R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) fragmentLogedUser
				.getView().findViewById(R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) fragmentLogedUser.getView()
				.findViewById(R.id.userView);

		Assert.assertEquals(LinearLayout.GONE, noSessionView.getVisibility());
		Assert.assertEquals(LinearLayout.GONE, errorView.getVisibility());
		Assert.assertTrue((RelativeLayout.VISIBLE == loadingView
				.getVisibility())
				|| (ScrollView.VISIBLE == userView.getVisibility()));
	}

	@Test
	public void retrieveUserInfoShouldNotBeCalledIfUserIsNotLoged() {
		Mockito.verify(iRetrieveUserInfoService, Mockito.times(0))
				.retrieveUserInfo(fragmentNotLogedUser);
	}

	@Test
	public void retrieveUserInfoShouldBeCalledOnceIfUserIsLoged() {
		Mockito.verify(iRetrieveUserInfoService, Mockito.times(1))
				.retrieveUserInfo(fragmentLogedUser);
	}

	@Test
	public void userFragmentChangeToUserShowsFragmentOnItemClick() {
		Button button = (Button) fragmentLogedUser.getView().findViewById(
				R.id.buyedAndReservedFunctionsButton);
		button.performClick();

		Assert.assertEquals(
				UserShowsFragment.class.getCanonicalName(),
				activityLogedUser.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void dialogShouldAppearsWhenUserTriesToLogout() {
		Button button = (Button) fragmentLogedUser.getView().findViewById(
				R.id.logoutButton);
		button.performClick();

		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		Assert.assertTrue(dialog.isShowing());
	}

	@Test
	public void noSessionViewShouldBeVisibleWhenUserConfirmsLogout() {
		Button button = (Button) fragmentLogedUser.getView().findViewById(
				R.id.logoutButton);
		button.performClick();

		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

		LinearLayout noSessionView = (LinearLayout) fragmentLogedUser.getView()
				.findViewById(R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) fragmentLogedUser.getView()
				.findViewById(R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) fragmentLogedUser
				.getView().findViewById(R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) fragmentLogedUser.getView()
				.findViewById(R.id.userView);

		Assert.assertFalse(dialog.isShowing());

		Assert.assertEquals(LinearLayout.VISIBLE, noSessionView.getVisibility());
		Assert.assertEquals(LinearLayout.GONE, errorView.getVisibility());
		Assert.assertEquals(RelativeLayout.GONE, loadingView.getVisibility());
		Assert.assertEquals(ScrollView.GONE, userView.getVisibility());
		
		Assert.assertFalse(UserManager.getInstance().isUserLoged());
	}
}
