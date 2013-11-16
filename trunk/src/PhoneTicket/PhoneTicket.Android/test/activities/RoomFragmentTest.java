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

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DiscountFragment;
import phoneticket.android.activities.fragments.RoomFragment;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.mock.MockRetrieveRoomInfoService;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.utils.UserManager;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.GridView;

@RunWith(RobolectricTestRunner.class)
public class RoomFragmentTest {

	@Mock
	private IRetrieveRoomInfoService roomInfoService;
	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;
	@Mock
	private IRetrieveDiscountService retrieveDiscountService;
	@Mock
	private IRegisterReservationService reservationService;
	@Mock
	private IRegisterPurchaseService purchaseService;
	@Mock
	private IRegisterDiscountsService discountsService;

	private RoomFragment fragment;
	private MasterActivity activity;
	private Ticket ticket;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

		module.addBinding(IRetrieveRoomInfoService.class, roomInfoService);
		module.addBinding(IRetrieveMovieListService.class,
				iRetrieveMovieListService);
		module.addBinding(IRetrieveDiscountService.class,
				retrieveDiscountService);

		module.addBinding(IRegisterReservationService.class, reservationService);

		module.addBinding(IRegisterPurchaseService.class, purchaseService);

		module.addBinding(IRegisterDiscountsService.class, discountsService);

		TestModule.setUp(this, module);

		activity = Robolectric.buildActivity(MasterActivity.class).create()
				.resume().get();

		ticket = new Ticket(0, "a", "a", 0, "a", 1, "a", "a", 1, 20);
		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);
		UserManager.getInstance().setCredentials("pepe", "pepe");
		UserManager.getInstance().loginUserWithId(0, true);
		activity.onFunctionSelected(ticket);

		fragment = (RoomFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

	}

	@Test
	public void roomFragmentCallRetrieveRoomInfoServiceOnCreate() {
		Mockito.verify(roomInfoService, Mockito.times(1)).retrieveRoomInfo(
				fragment, ticket.getFunctionId());
	}

	@Test
	public void roomFragmentNotChangeFragmentWhenNoArmChairIsSelected() {
		new MockRetrieveRoomInfoService().retrieveRoomInfo(fragment,
				ticket.getFunctionId());

		Button button = (Button) fragment.getView().findViewById(
				R.id.selectionFinished);

		button.performClick();

		Assert.assertEquals(
				RoomFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void roomFragmentChangeFragmentWhenAtLeastOneArmChairIsSelected() {
		new MockRetrieveRoomInfoService().retrieveRoomInfo(fragment,
				ticket.getFunctionId());

		GridView gridView = (GridView) fragment.getView().findViewById(
				R.id.armChairSelection);

		gridView.getOnItemClickListener().onItemClick(null, null, 0, 0);

		Button button = (Button) fragment.getView().findViewById(
				R.id.selectionFinished);

		button.performClick();

		Assert.assertEquals(
				DiscountFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}
}
