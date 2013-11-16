package activities;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

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
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowPreferenceManager;

import phoneticket.android.R;
import phoneticket.android.activities.BuyTicketsActivity;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.fragments.DiscountFragment;
import phoneticket.android.activities.fragments.MovieListFragment;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.DiscountCountable;
import phoneticket.android.model.PostedArmChair;
import phoneticket.android.model.PostedDiscounts;
import phoneticket.android.model.PurchaseTicket;
import phoneticket.android.model.ReserveTicket;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.mock.MockRetrieveDiscountsService;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.impl.mock.MockRegisterReservationService;
import phoneticket.android.utils.UserManager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;

@RunWith(RobolectricTestRunner.class)
public class DiscountsFragmentTest {

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

	private DiscountFragment fragment;
	private MasterActivity activity;
	private Ticket ticket;
	private List<ArmChair> armChairs;
	private ArmChair armChair;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		TestModule module = new TestModule();

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

		ticket = new Ticket(0, "a", "a", 0, "a", 1, "2015/10/10", "22:22", 1,
				20);
		SharedPreferences sharedPreferences = ShadowPreferenceManager
				.getDefaultSharedPreferences(Robolectric.application
						.getApplicationContext());
		UserManager.initialize(sharedPreferences);
		UserManager.getInstance().setCredentials("pepe", "pepe");
		UserManager.getInstance().loginUserWithId(0, true);
		armChairs = new ArrayList<ArmChair>();
		armChair = new ArmChair(0, 1, "1");
		armChairs.add(armChair);
		activity.onArmChairsSelected(armChairs, ticket);

		fragment = (DiscountFragment) activity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);

	}

	@Test
	public void discountsFragmentCallRetrieveDiscountsInfoServiceOnCreate() {
		Mockito.verify(retrieveDiscountService, Mockito.times(1))
				.retrieveDiscounts(fragment);
	}

	@Test
	public void discountFragmentChangeToMovieListFragmentWhenCancelIsSelected() {

		new MockRetrieveDiscountsService().retrieveDiscounts(fragment);

		Button cancel = ((Button) fragment.getView().findViewById(
				R.id.cancelarButton));

		cancel.performClick();

		Assert.assertEquals(
				MovieListFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void discountFragmentCallReserveServiceWhenFilmTimeIsMoreThanAnHour() {
		Button reserve = ((Button) fragment.getView().findViewById(
				R.id.reservarButton));

		new MockRetrieveDiscountsService().retrieveDiscounts(fragment);

		reserve.performClick();

		List<PostedArmChair> list = new ArrayList<PostedArmChair>();
		list.add(new PostedArmChair(1, 1));
		Mockito.verify(reservationService, Mockito.times(1)).reserveTicket(
				fragment, new ReserveTicket(ticket.getFunctionId(), list));
	}

	@Test
	public void discountFragmentNotCallReserveServiceWhenFilmTimeIsLessThanAnHour() {
		Button reserve = ((Button) fragment.getView().findViewById(
				R.id.reservarButton));
		// Button purchase = ((Button) fragment.getView().findViewById(
		// R.id.purchaseButton));

		new MockRetrieveDiscountsService().retrieveDiscounts(fragment);
		ticket.setFunctionDay("2013/10/10");

		reserve.performClick();
		List<PostedArmChair> list = new ArrayList<PostedArmChair>();
		list.add(new PostedArmChair(1, 1));
		Mockito.verify(reservationService, Mockito.times(0)).reserveTicket(
				fragment, new ReserveTicket(ticket.getFunctionId(), list));
	}

	@Test
	public void discountFragmentCallDiscountsServiceWhenReserve() {
		MockRetrieveDiscountsService mockDiscounts = new MockRetrieveDiscountsService();
		mockDiscounts.retrieveDiscounts(fragment);
		DiscountCountable dis = fragment.getDiscountsBaseInfo().get(0);
		dis.select(true);
		MockRegisterReservationService mock = new MockRegisterReservationService();
		mock.reserveTicket(fragment, null);
		List<PostedDiscounts> discountsSelected = new ArrayList<PostedDiscounts>();
		discountsSelected.add(new PostedDiscounts(dis.getId(), dis.getCount()));
		Mockito.verify(discountsService, Mockito.times(1)).registerDiscounts(
				fragment, discountsSelected, mock.getUuid());
	}

	@Test
	public void discountFragmentNotCallDiscountsServiceWhenReserve() {
		MockRetrieveDiscountsService mockDiscounts = new MockRetrieveDiscountsService();
		mockDiscounts.retrieveDiscounts(fragment);
		DiscountCountable dis = fragment.getDiscountsBaseInfo().get(0);
		dis.select(false);
		MockRegisterReservationService mock = new MockRegisterReservationService();
		mock.reserveTicket(fragment, null);
		List<PostedDiscounts> discountsSelected = new ArrayList<PostedDiscounts>();
		discountsSelected.add(new PostedDiscounts(dis.getId(), dis.getCount()));
		Mockito.verify(discountsService, Mockito.times(0)).registerDiscounts(
				fragment, discountsSelected, mock.getUuid());
	}

	@Test
	public void discountFragmentCallBuyTicketsActivity() {
		Button purchase = ((Button) fragment.getView().findViewById(
				R.id.purchaseButton));

		purchase.performClick();

		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		ComponentName c = intent.getComponent();
		String call = c.getClassName();
		assertNotNull(intent);
		Assert.assertEquals(BuyTicketsActivity.class.getName(), call);

	}

	@Test
	public void discountFragmentCallPurchaseServiceWhenRetrievePurchaseData() {
		fragment.onPurchaseDataResult("", "", "", 1);

		List<PostedArmChair> list = new ArrayList<PostedArmChair>();
		list.add(new PostedArmChair(1, 1));
		PurchaseTicket purchaseTicket = new PurchaseTicket(
				ticket.getFunctionId(), list, "", "", "", 1);

		Mockito.verify(purchaseService, Mockito.times(1)).purchaseTicket(
				fragment, purchaseTicket);
	}

	@Test
	public void discountFragmentCallPurchaseServiceOnButtonPress() {
		fragment.onPurchaseDataResult("", "", "", 1);

		Button purchase = ((Button) fragment.getView().findViewById(
				R.id.purchaseButton));

		purchase.performClick();
		List<PostedArmChair> list = new ArrayList<PostedArmChair>();
		list.add(new PostedArmChair(1, 1));
		PurchaseTicket purchaseTicket = new PurchaseTicket(
				ticket.getFunctionId(), list, "", "", "", 1);

		Mockito.verify(purchaseService, Mockito.times(2)).purchaseTicket(
				fragment, purchaseTicket);
	}
}
