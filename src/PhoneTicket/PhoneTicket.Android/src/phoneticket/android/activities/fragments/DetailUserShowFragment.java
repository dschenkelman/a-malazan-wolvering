package phoneticket.android.activities.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.BuyTicketsActivity;
import phoneticket.android.activities.MasterActivity.ICalendarDataSource;
import phoneticket.android.activities.MasterActivity.IOnPurchaseDataResultListener;
import phoneticket.android.activities.QRCodeActivity;
import phoneticket.android.activities.fragments.dialogs.ConfirmShowReserveCancelationDialogFragment;
import phoneticket.android.activities.fragments.dialogs.ConfirmShowReserveCancelationDialogFragment.IConfirmShowReserveCancelationDialogDelegate;
import phoneticket.android.activities.interfaces.IRibbonChangeMenuListener;
import phoneticket.android.activities.interfaces.IShareActionListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.activities.interfaces.IUserShowsActionListener;
import phoneticket.android.model.CreditCardData;
import phoneticket.android.model.DetailUserShow;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.model.IDiscount;
import phoneticket.android.model.ISeat;
import phoneticket.android.services.delete.ICancelUserShowService;
import phoneticket.android.services.delete.ICancelUserShowServiceDelegate;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.services.post.IConfirmReservationService;
import phoneticket.android.services.post.IConfirmReservationServiceDelegate;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import roboguice.fragment.RoboFragment;

public class DetailUserShowFragment extends RoboFragment implements
		IRetrieveUserShowInfoServiceDelegate,
		IConfirmShowReserveCancelationDialogDelegate,
		ICancelUserShowServiceDelegate, IOnPurchaseDataResultListener,
		IConfirmReservationServiceDelegate, ICalendarDataSource {

	public static final String TAG = "DetailUserShowFragment.tag";
	public static final String USER_SHOW_INFO = "usershow.info";
	public static final String USER_SHOW_INFO_IS_BOUGHT = "usershow.info.isbought";

	public static final String STATE_USER_SHOW_ID = "usershow.id";
	private static final String STATE_USER_SHOW_MOVIE_NAME = "usershow.moviename";
	private static final String STATE_USER_SHOW_TIME = "usershow.showtime";
	private static final String STATE_USER_SHOW_COMPLEX_ADDRESS = "usershow.complexaddress";
	private static final String STATE_USER_SHOW_IS_BOUGHT = "usershow.isbought";
	private static final String STATE_USER_SHOW_QR_STRING = "usershow.qrstring";
	private static final String STATE_USER_SHOW_PRICE = "usershow.price";
	private static final String STATE_USER_SHOW_SEATS = "usershow.seats";

	private static final String SEPARATOR_STATE_USER_SHOW_SEATS = "a";
	private static final String SEPARATOR_STATE_USER_ITEM_SHOW_SEATS = ",";

	private boolean ignoreServicesCallbacks;

	private String showId;
	private boolean isBought;

	@Inject
	private IRetrieveUserShowInfoService infoService;
	@Inject
	private ICancelUserShowService cancelService;
	@Inject
	private IConfirmReservationService confirmService;

	private IDetailUserShow userShow;

	private IShareButtonsVisibilityListener shareButtonsVisibilityListener;
	private IShareActionListener shareActionListener;
	private IUserShowsActionListener userShowStateListener;
	private IRibbonChangeMenuListener ribbonListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_user_shows,
				container, false);

		showId = getArguments()
				.getString(DetailUserShowFragment.USER_SHOW_INFO);
		isBought = getArguments().getBoolean(
				DetailUserShowFragment.USER_SHOW_INFO_IS_BOUGHT);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		ribbonListener.setOnUserMenu();
		ignoreServicesCallbacks = false;

		loadDetailUserShow();

		if (shouldRetrieveUserShow()) {
			onRetrieveUserShowAction();
		} else {
			populateUserShowView();
			showUserShowListLayout();
		}

		((Button) getView().findViewById(R.id.reloadDataButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onRetrieveUserShowAction();
					}
				});
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
		saveDetailUserShow();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		deleteDetailUserShow();
		shareButtonsVisibilityListener.hideCalendarButton();
		shareButtonsVisibilityListener.hideFacebookShareButton();
		shareButtonsVisibilityListener.hideTwitterShareButton();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			shareButtonsVisibilityListener = (IShareButtonsVisibilityListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IShareButtonsVisibilityListener");
		}
		try {
			shareActionListener = (IShareActionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IShareActionListener");
		}
		try {
			userShowStateListener = (IUserShowsActionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IUserShowsActionListener");
		}
		try {
			ribbonListener = (IRibbonChangeMenuListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IRibbonChangeMenuListener");
		}
	}

	private void deleteDetailUserShow() {
		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();
		editor.remove(STATE_USER_SHOW_ID);
		editor.commit();
	}

	private void saveDetailUserShow() {
		if (null != userShow) {
			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.putString(STATE_USER_SHOW_ID, userShow.getId());
			editor.putBoolean(STATE_USER_SHOW_IS_BOUGHT, userShow.isBought());
			editor.putString(STATE_USER_SHOW_MOVIE_NAME,
					userShow.getMovieTitle());
			editor.putString(STATE_USER_SHOW_TIME,
					userShow.getShowDateAndTime());
			editor.putString(STATE_USER_SHOW_COMPLEX_ADDRESS,
					userShow.getComplexAddress());
			editor.putString(STATE_USER_SHOW_QR_STRING, userShow.getQRString());
			editor.putInt(STATE_USER_SHOW_PRICE,
					userShow.getSingleTicketShowPrice());

			String seatStream = "";
			for (ISeat seat : userShow.getSeats()) {
				seatStream += seat.getRow()
						+ SEPARATOR_STATE_USER_ITEM_SHOW_SEATS
						+ seat.getColumn() + SEPARATOR_STATE_USER_SHOW_SEATS;
			}
			editor.putString(STATE_USER_SHOW_SEATS, seatStream);

			editor.commit();
		}
	}

	private void loadDetailUserShow() {
		SharedPreferences preferences = getActivity().getPreferences(0);
		String invalidId = "invalid";
		String id = preferences.getString(STATE_USER_SHOW_ID, invalidId);

		if (!id.equalsIgnoreCase(invalidId)) {
			doLoadDetailUserShow();
		}
	}

	private void doLoadDetailUserShow() {
		int price;
		boolean isBought;
		String id, movieName, showTime, complexAddress, qrstring, seatsStream;

		try {
			SharedPreferences preferences = getActivity().getPreferences(0);
			id = preferences.getString(STATE_USER_SHOW_ID, "");
			price = preferences.getInt(STATE_USER_SHOW_PRICE, 0);
			isBought = preferences.getBoolean(STATE_USER_SHOW_IS_BOUGHT, false);
			movieName = preferences.getString(STATE_USER_SHOW_MOVIE_NAME, "-");
			showTime = preferences.getString(STATE_USER_SHOW_TIME, "-");
			complexAddress = preferences.getString(
					STATE_USER_SHOW_COMPLEX_ADDRESS, "-");
			qrstring = preferences.getString(STATE_USER_SHOW_QR_STRING, "-");

			DetailUserShow userShow = new DetailUserShow(id, isBought,
					movieName, showTime, complexAddress, qrstring, price);

			seatsStream = preferences.getString(STATE_USER_SHOW_SEATS, "");
			for (String seatString : seatsStream
					.split(SEPARATOR_STATE_USER_SHOW_SEATS)) {
				String pair[] = seatString
						.split(SEPARATOR_STATE_USER_ITEM_SHOW_SEATS);
				if (2 == pair.length) {
					userShow.addSeat(Integer.parseInt(pair[0]),
							Integer.parseInt(pair[1]));
				}
			}
			this.userShow = userShow;
		} catch (Exception e) {
			this.userShow = null;
		}
	}

	private boolean shouldRetrieveUserShow() {
		return null == userShow;
	}

	private void onRetrieveUserShowAction() {
		showLoadingLayout();
		infoService.retrieveUserShowInfo(this, showId);
	}

	protected void onGetQRCodeAction() {
		Intent intent = new Intent(getActivity(), QRCodeActivity.class);
		intent.putExtra(QRCodeActivity.EXTRA_QR_STRING, userShow.getQRString());
		startActivity(intent);
	}

	protected void onBuyReservationAction() {
		Intent intent = new Intent(getActivity(), BuyTicketsActivity.class);
		startActivityForResult(intent, 31);
	}

	protected void onCancelReservationAction() {
		ConfirmShowReserveCancelationDialogFragment dialog = new ConfirmShowReserveCancelationDialogFragment();
		dialog.show(getFragmentManager(), "dialog.confirmation");
	}

	private void populateUserShowView() {
		shareButtonsVisibilityListener.showFacebookShareButton();
		shareButtonsVisibilityListener.showTwitterShareButton();
		shareButtonsVisibilityListener.showCalendarButton();
		shareActionListener.setShareMessageOnFacebook("Me voy a ver "
				+ userShow.getMovieTitle() + " a CINEMAR");

		TextView nameAndTimeTextView = (TextView) getView().findViewById(
				R.id.movieNameAndShowTimeTextView);
		TextView cinemaAddressTextView = (TextView) getView().findViewById(
				R.id.complexAddressTextView);
		TextView showPriceTextView = (TextView) getView().findViewById(
				R.id.showPriceTextView);
		TextView placesCountTextView = (TextView) getView().findViewById(
				R.id.placesCountTextView);
		TextView discountsTextView = (TextView) getView().findViewById(
				R.id.discountsTextView);
		TextView placesCodesTextView = (TextView) getView().findViewById(
				R.id.placesCodesTextView);

		Button cancelButton = (Button) getView().findViewById(
				R.id.cancelShowButton);
		Button buyButton = (Button) getView().findViewById(
				R.id.boughtShowButton);
		Button qrCodeButton = (Button) getView()
				.findViewById(R.id.qrCodeButton);

		nameAndTimeTextView.setText(userShow.getMovieTitle() + " - "
				+ userShow.getShowDateAndTime());
		cinemaAddressTextView.setText(userShow.getComplexAddress());
		showPriceTextView.setText("Precio final: $"
				+ userShow.getShowPrice(true));
		placesCountTextView.setText(userShow.getSeats().size() + " Entrada"
				+ ((1 != userShow.getSeats().size()) ? "s" : ""));

		if (0 == userShow.getDiscounts().size()) {
			discountsTextView.setText("No se seleciconaron descuentos.");
		} else {
			String discountText = "";
			int number = 1;
			for (IDiscount discount : userShow.getDiscounts()) {
				discountText += discount.getCount() + ": "
						+ discount.getDescription();
				if (userShow.getDiscounts().size() != number) {
					discountText += "\n";
				}
				number++;
			}
			discountsTextView.setText(discountText);
		}

		String seatCodes = "";
		if (0 < userShow.getSeats().size()) {
			for (ISeat seat : userShow.getSeats()) {
				String code = Character.toString(Character.toChars(64 + seat
						.getRow())[0]);
				seatCodes += code + seat.getColumn() + " ";
			}
		}

		placesCodesTextView.setText(seatCodes);

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCancelReservationAction();
			}
		});
		buyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBuyReservationAction();
			}
		});
		qrCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGetQRCodeAction();
			}
		});

		if (userShow.isBought()) {
			cancelButton.setVisibility(Button.GONE);
			buyButton.setVisibility(Button.GONE);
		}

		if (userShow.isBought()) {
			shareActionListener
					.setShareOnTwitterMessage("He comprado entradas para "
							+ userShow.getMovieTitle() + " en CINEMARK");
		} else {
			shareActionListener
					.setShareOnTwitterMessage("He reservado entradas para "
							+ userShow.getMovieTitle() + " en CINEMARK");
		}
	}

	private void showUserShowListLayout() {
		layoutVisibility(LinearLayout.GONE, RelativeLayout.GONE,
				ScrollView.VISIBLE);
	}

	private void showLoadingLayout() {
		TextView message = (TextView) ((RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout))
				.findViewById(R.id.downloadingDataTextView);
		message.setText("Cargando los datos. Por Favor, espere un momento.");
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ScrollView.GONE);
	}

	private void showErrorLayout() {
		layoutVisibility(LinearLayout.VISIBLE, RelativeLayout.GONE,
				ScrollView.GONE);
	}

	private void showCancelingLayout() {
		TextView message = (TextView) ((RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout))
				.findViewById(R.id.downloadingDataTextView);
		message.setText("Cancelando la reserva. Por Favor, espere un momento");
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ScrollView.GONE);
	}

	private void showConfirmingLayout() {
		TextView message = (TextView) ((RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout))
				.findViewById(R.id.downloadingDataTextView);
		message.setText("Confirmando la reserva. Por Favor, espere un momento");
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ScrollView.GONE);
	}

	private void layoutVisibility(int erroVisibility, int loadingVisibility,
			int myShowsVisibilty) {
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ScrollView myShows = (ScrollView) getView().findViewById(
				R.id.dataScrollView);

		errorView.setVisibility(erroVisibility);
		loadingView.setVisibility(loadingVisibility);
		myShows.setVisibility(myShowsVisibilty);
	}

	@Override
	public void retrieveUserShowInfoFinish(
			IRetrieveUserShowInfoService delegate, IDetailUserShow userShow) {
		if (false == ignoreServicesCallbacks) {
			this.userShow = userShow;
			this.userShow.setId(showId);
			if (isBought) {
				this.userShow.setBought();
			}
			saveDetailUserShow();
			populateUserShowView();
			showUserShowListLayout();
		}
	}

	@Override
	public void retrieveUserShowInfoFinishWithError(
			IRetrieveUserShowInfoService delegate, int errorCode) {
		if (false == ignoreServicesCallbacks) {
			showErrorLayout();
		}
	}

	@Override
	public void cancelUserShowFinished(ICancelUserShowService delegate) {
		if (false == ignoreServicesCallbacks) {
			if (null != userShowStateListener)
				userShowStateListener.onCanceledUserShowAction(userShow);
		}
	}

	@Override
	public void cancelUserShowFinishedWithError(
			ICancelUserShowService delegate, int errorCode) {
		if (false == ignoreServicesCallbacks) {
			showUserShowListLayout();

			DialogFragment dialog = new DialogFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setMessage(
							"Error al cancelar la reserva. Vuelva a intentarlo.")
							.setTitle("Error")
							.setPositiveButton(R.string.errorDialogContinue,
									null);
					return builder.create();
				}
			};
			dialog.show(getFragmentManager(), "dialog.confirmation");
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		showCancelingLayout();
		shareButtonsVisibilityListener.hideCalendarButton();
		shareButtonsVisibilityListener.hideFacebookShareButton();
		shareButtonsVisibilityListener.hideTwitterShareButton();
		cancelService.cancelUserShow(this, this.userShow);
	}

	@Override
	public void onPurchaseDataResult(String cardNumber, String securityNumber,
			String expiration, int cardCompany) {
		showConfirmingLayout();
		confirmService.confirmReservation(this, new CreditCardData(cardNumber,
				securityNumber, cardCompany, expiration), showId);
	}

	@Override
	public void confirmReservationFinished(IConfirmReservationService service) {
		if (false == ignoreServicesCallbacks) {
			this.userShow.setBought();
			populateUserShowView();
			showUserShowListLayout();

			DialogFragment dialog = new DialogFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setMessage(
							"Usted ha comprado la reserva con exito.")
							.setTitle("Exito")
							.setPositiveButton(R.string.messageDialogContinue,
									null);
					return builder.create();
				}
			};
			dialog.show(getFragmentManager(), "dialog.message");

			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.remove(UserShowsFragment.STATE_SHOWS_STREAM);
			editor.commit();
		}
	}

	@Override
	public void confirmReservationFinishedWithError(
			IConfirmReservationService service, int errorCode) {
		if (false == ignoreServicesCallbacks) {
			showUserShowListLayout();

			DialogFragment dialog = new DialogFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setMessage(
							"Error al efectivizar la reserva. Vuelva a intentarlo.")
							.setTitle("Error")
							.setPositiveButton(R.string.errorDialogContinue,
									null);
					return builder.create();
				}
			};
			dialog.show(getFragmentManager(), "dialog.error");
		}
	}

	@Override
	public String getEventTitle() {
		return "Función de " + userShow.getMovieTitle() + " en CINEMAR";
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public long getStartDateInMilliseconds() {
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR);

			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy'/'dd'/'MM mm:HH'Hs'");
			Date date = sdf.parse(year + "/" + userShow.getShowDateAndTime());
			Calendar cal = Calendar.getInstance(Locale.getDefault());

			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getDurationInMilliseconds() {
		return userShow.getShowDuration() * 60 * 1000;
	}

}
