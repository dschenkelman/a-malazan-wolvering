package activities;

import java.util.ArrayList;

import junit.framework.Assert;
import module.TestModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import android.content.SharedPreferences;

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.CinemasFragment;
import phoneticket.android.activities.fragments.DetailCinemaFragment;
import phoneticket.android.activities.fragments.DetailMovieFragment;
import phoneticket.android.activities.fragments.DetailUserShowFragment;
import phoneticket.android.activities.fragments.DiscountFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.activities.fragments.RoomFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.fragments.UserShowsFragment;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.IMyShow;
import phoneticket.android.model.MyShow;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.utils.UserManager;

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

	@Mock
	private IRetrieveRoomInfoService roomInfoService;

	@Mock
	private IRetrieveDiscountService retrieveDiscountService;
	@Mock
	private IRegisterReservationService reservationService;
	@Mock
	private IRegisterPurchaseService purchaseService;
	@Mock
	private IRegisterDiscountsService discountsService;

	@Mock
	private IRetrieveMyShowsService myShowsService;

	@Mock
	private IRetrieveUserShowInfoService infoService;
	@Mock
	private ICancelUserShowService cancelService;
	@Mock
	private IConfirmReservationService confirmService;

	@Mock
	private IRetrieveUserInfoService userInfoService;

	private MasterActivity activity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();
		module.addBinding(IRetrieveDiscountService.class,
				retrieveDiscountService);
		module.addBinding(IRegisterPurchaseService.class, purchaseService);
		module.addBinding(IRetrieveUserInfoService.class, userInfoService);
		module.addBinding(IRetrieveUserShowInfoService.class, infoService);
		module.addBinding(ICancelUserShowService.class, cancelService);
		module.addBinding(IConfirmReservationService.class, confirmService);
		module.addBinding(IRetrieveMyShowsService.class, myShowsService);
		module.addBinding(IRegisterReservationService.class, reservationService);
		module.addBinding(IRegisterDiscountsService.class, discountsService);
		module.addBinding(IRetrieveCinemaListService.class, cinemaListService);
		module.addBinding(IRetrieveCinemaInfoService.class, cinemaInfoService);
		module.addBinding(IRetrieveMovieInfoService.class, movieInfoService);
		module.addBinding(IRetrieveMovieFunctionsService.class,
				movieFunctionsService);
		module.addBinding(IRetrieveMovieListService.class, movieListService);
		module.addBinding(IRetrieveRoomInfoService.class, roomInfoService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);
		UserManager.getInstance().setCredentials("pepe", "pepe");
		UserManager.getInstance().loginUserWithId(2, true);

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
	public void changeToDetailMovieFragmentOnMovielistItemSelected() {
		activity.onMovielistItemSelected(0, "");
		Assert.assertEquals(
				DetailMovieFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToDetailCinemaFragmentOnCinemaSelected() {
		activity.onCinemaSelected(0, "");
		Assert.assertEquals(
				DetailCinemaFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToRoomFragmentOnFunctionSelected() {
		activity.onFunctionSelected(new Ticket(0, "", "", 1, "", 1, "", "", 1,
				1));
		Assert.assertEquals(
				RoomFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToDiscountsFragmentOnArmChairsSelected() {
		ArrayList<ArmChair> armChairs = new ArrayList<ArmChair>();
		activity.onArmChairsSelected(armChairs, new Ticket(0, "", "", 1, "", 1,
				"", "", 1, 1));
		Assert.assertEquals(
				DiscountFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToUserShowsFragmentOnShowUserShowsAction() {
		activity.onShowUserShowsAction();
		Assert.assertEquals(
				UserShowsFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());

	}

	@Test
	public void changeToDetailUserShowFragmentOnShowDetailUserShowAction() {
		IMyShow userShow = new MyShow("2", false, "pa", "12:12", "asda");
		activity.onShowDetailUserShowAction(userShow);
		Assert.assertEquals(
				DetailUserShowFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void changeToMovieListFragmentOntoMovieList() {
		IMyShow userShow = new MyShow("2", false, "pa", "12:12", "asda");
		activity.onShowDetailUserShowAction(userShow);
		Assert.assertEquals(
				DetailUserShowFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
		activity.toMovieList();
		Assert.assertEquals(
				MovieListFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

}
