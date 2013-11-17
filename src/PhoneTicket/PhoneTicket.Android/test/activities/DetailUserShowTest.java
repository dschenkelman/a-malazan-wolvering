package activities;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowPreferenceManager;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import phoneticket.android.R;
import phoneticket.android.activities.BuyTicketsActivity;
import phoneticket.android.activities.MasterActivity;
import phoneticket.android.activities.QRCodeActivity;
import phoneticket.android.activities.fragments.DetailUserShowFragment;
import phoneticket.android.activities.fragments.UserFragment;
import phoneticket.android.activities.fragments.UserShowsFragment;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.model.IDiscount;
import phoneticket.android.model.IMyShow;
import phoneticket.android.model.ISeat;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.delete.mock.MockCancelUserShowService;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.utils.UserManager;

@RunWith(RobolectricTestRunner.class)
public class DetailUserShowTest {

	@Mock
	private IRetrieveMovieListService iRetrieveMovieListService;
	@Mock
	private IRetrieveUserInfoService iRetrieveUserInfoService;
	@Mock
	private IRetrieveMyShowsService iRetrieveMyShowsService;
	@Mock
	private IRetrieveUserShowInfoService iRetrieveUserShowInfoService;
	@Mock
	private ICancelUserShowService iCancelUserShowService;
	@Mock
	private IConfirmReservationService iConfirmUserShowService;

	private ListView userShowsList;
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
		module.addBinding(ICancelUserShowService.class, iCancelUserShowService);
		module.addBinding(IConfirmReservationService.class,
				iConfirmUserShowService);

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

		getMockRectrieveMyShowsService().retrieveMyShows(userShowsFragment);

		userShowsList = (ListView) userShowsFragment.getView().findViewById(
				R.id.myShowsListView);
	}

	@Test
	public void retrieveDetailUserShowInfoShouldBeCalledOnceOnCreate() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);

		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		Mockito.verify(iRetrieveUserShowInfoService, Mockito.times(1))
				.retrieveUserShowInfo(fragment, "id1");
	}

	@Test
	public void detailViewShouldBeVisibleAfterRetrievingUserShowInfoData() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);

		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		LinearLayout errorView = (LinearLayout) fragment.getView()
				.findViewById(R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) fragment.getView()
				.findViewById(R.id.loadingDataLayout);
		ScrollView detailView = (ScrollView) fragment.getView().findViewById(
				R.id.dataScrollView);

		Assert.assertEquals(LinearLayout.GONE, errorView.getVisibility());
		Assert.assertEquals(RelativeLayout.VISIBLE, loadingView.getVisibility());
		Assert.assertEquals(ScrollView.GONE, detailView.getVisibility());

		getUserShowInfoService(false).retrieveUserShowInfo(fragment, "id1");

		Assert.assertEquals(LinearLayout.GONE, errorView.getVisibility());
		Assert.assertEquals(RelativeLayout.GONE, loadingView.getVisibility());
		Assert.assertEquals(ScrollView.VISIBLE, detailView.getVisibility());
	}

	@Test
	public void reservationButtonsShouldBeVisibleOnReservationUserShow() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);

		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		getUserShowInfoService(false).retrieveUserShowInfo(fragment, "id");

		Button cancelButton = (Button) fragment.getView().findViewById(
				R.id.cancelShowButton);
		Button buyButton = (Button) fragment.getView().findViewById(
				R.id.boughtShowButton);
		Button qrCodeButton = (Button) fragment.getView().findViewById(
				R.id.qrCodeButton);

		Assert.assertEquals(Button.VISIBLE, cancelButton.getVisibility());
		Assert.assertEquals(Button.VISIBLE, buyButton.getVisibility());
		Assert.assertEquals(Button.VISIBLE, qrCodeButton.getVisibility());
	}

	@Test
	public void reservationButtonsShouldBeVisibleOnPurchaseUserShow() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);

		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);

		getUserShowInfoService(true).retrieveUserShowInfo(fragment, "id");

		Button cancelButton = (Button) fragment.getView().findViewById(
				R.id.cancelShowButton);
		Button buyButton = (Button) fragment.getView().findViewById(
				R.id.boughtShowButton);
		Button qrCodeButton = (Button) fragment.getView().findViewById(
				R.id.qrCodeButton);

		Assert.assertEquals(Button.GONE, cancelButton.getVisibility());
		Assert.assertEquals(Button.GONE, buyButton.getVisibility());
		Assert.assertEquals(Button.VISIBLE, qrCodeButton.getVisibility());
	}

	@Test
	public void detailUserShowFragmentChangeToQRCodeActivityOnItemClick() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);
		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);
		getUserShowInfoService(true).retrieveUserShowInfo(fragment, "id");

		Button qrCodeButton = (Button) fragment.getView().findViewById(
				R.id.qrCodeButton);
		qrCodeButton.performClick();

		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		ComponentName c = intent.getComponent();
		String call = c.getClassName();
		assertNotNull(intent);
		Assert.assertEquals(QRCodeActivity.class.getName(), call);
	}

	@Test
	public void dialogShouldAppearsWhenUserTriesToCancelReservation() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);
		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);
		getUserShowInfoService(false).retrieveUserShowInfo(fragment, "id");

		Button cancelButton = (Button) fragment.getView().findViewById(
				R.id.cancelShowButton);
		cancelButton.performClick();

		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		Assert.assertTrue(dialog.isShowing());
	}

	@Test
	public void backActionMustBePerformedWhenUserConfirmsAReserveCancelation() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);
		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);
		getUserShowInfoService(false).retrieveUserShowInfo(fragment, "id");

		Button cancelButton = (Button) fragment.getView().findViewById(
				R.id.cancelShowButton);
		cancelButton.performClick();

		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

		new MockCancelUserShowService().cancelUserShow(fragment, null);

		Assert.assertEquals(
				UserShowsFragment.class.getCanonicalName(),
				activity.getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container).getClass()
						.getCanonicalName());
	}

	@Test
	public void detailUserShowFragmentChangeToBuyTicketActivityOnItemClick() {
		userShowsList.getOnItemClickListener().onItemClick(null, null, 0, 0);
		DetailUserShowFragment fragment = (DetailUserShowFragment) activity
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_container);
		getUserShowInfoService(true).retrieveUserShowInfo(fragment, "id");

		Button buyButton = (Button) fragment.getView().findViewById(
				R.id.boughtShowButton);
		buyButton.performClick();

		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		ComponentName c = intent.getComponent();
		String call = c.getClassName();
		assertNotNull(intent);
		Assert.assertEquals(BuyTicketsActivity.class.getName(), call);
	}

	private IRetrieveMyShowsService getMockRectrieveMyShowsService() {
		return new IRetrieveMyShowsService() {
			@Override
			public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
				Collection<IMyShow> shows = new ArrayList<IMyShow>();
				shows.add(new IMyShow() {
					@Override
					public boolean isBought() {
						return false;
					}

					@Override
					public String getShowDateAndTime() {
						return "11/12 12:45 Hs";
					}

					@Override
					public String getMovieTitle() {
						return "Las Horas";
					}

					@Override
					public String getId() {
						return "id1";
					}

					@Override
					public String getComplexAddress() {
						return "Callao 123";
					}
				});
				shows.add(new IMyShow() {
					@Override
					public boolean isBought() {
						return true;
					}

					@Override
					public String getShowDateAndTime() {
						return "19/12 12:45 Hs";
					}

					@Override
					public String getMovieTitle() {
						return "Dogville";
					}

					@Override
					public String getId() {
						return "id2";
					}

					@Override
					public String getComplexAddress() {
						return "Callao 123";
					}
				});
				delegate.retrieveMyShowsServiceFinished(this, shows);
			}
		};
	}

	private IRetrieveUserShowInfoService getUserShowInfoService(
			final boolean isBought) {
		return new IRetrieveUserShowInfoService() {
			@Override
			public void retrieveUserShowInfo(
					IRetrieveUserShowInfoServiceDelegate delegate,
					final String userShowId) {
				delegate.retrieveUserShowInfoFinish(this,
						new IDetailUserShow() {
							@Override
							public boolean isBought() {
								return isBought;
							}

							@Override
							public String getShowDateAndTime() {
								return "11/19 22:22 Hs";
							}

							@Override
							public String getMovieTitle() {
								return "Matrix";
							}

							@Override
							public String getId() {
								return userShowId;
							}

							@Override
							public String getComplexAddress() {
								return "Callao 123";
							}

							@Override
							public void setId(String showId) {
							}

							@Override
							public void setBought() {
							}

							@Override
							public int getSingleTicketShowPrice() {
								return 100;
							}

							@Override
							public int getShowPrice(boolean whitDiscount) {
								return 100;
							}

							@Override
							public int getShowDuration() {
								return 90;
							}

							@Override
							public Collection<ISeat> getSeats() {
								return new ArrayList<ISeat>();
							}

							@Override
							public String getQRString() {
								return "id1";
							}

							@Override
							public Collection<IDiscount> getDiscounts() {
								return new ArrayList<IDiscount>();
							}
						});
			}
		};
	}
}
