package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.BuyTicketsActivity;
import phoneticket.android.activities.QRCodeActivity;
import phoneticket.android.activities.fragments.dialogs.ConfirmShowReserveCancelationDialogFragment;
import phoneticket.android.activities.fragments.dialogs.ConfirmShowReserveCancelationDialogFragment.IConfirmShowReserveCancelationDialogDelegate;
import phoneticket.android.activities.interfaces.IShareActionListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.activities.interfaces.IUserShowsActionListener;
import phoneticket.android.model.DetailUserShow;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.services.post.ICancelUserShowService;
import phoneticket.android.services.post.ICancelUserShowServiceDelegate;
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
		ICancelUserShowServiceDelegate {

	public static final String TAG = "DetailUserShowFragment.tag";
	public static final String USER_SHOW_INFO = "usershow.info";

	private static final String STATE_USER_SHOW_ID = "usershow.id";
	private static final String STATE_USER_SHOW_MOVIE_NAME = "usershow.moviename";
	private static final String STATE_USER_SHOW_TIME = "usershow.showtime";
	private static final String STATE_USER_SHOW_COMPLEX_ADDRESS = "usershow.complexaddress";
	private static final String STATE_USER_SHOW_IS_BOUGHT = "usershow.isbought";
	private static final String STATE_USER_SHOW_QR_STRING = "usershow.qrstring";
	private static final String STATE_USER_SHOW_PRICE = "usershow.price";

	private boolean ignoreServicesCallbacks;

	private int showId;

	@Inject
	private IRetrieveUserShowInfoService infoService;
	@Inject
	private ICancelUserShowService cancelService;

	private IDetailUserShow userShow;

	private IShareButtonsVisibilityListener shareButtonsVisibilityListener;
	private IShareActionListener shareActionListener;
	private IUserShowsActionListener userShowStateListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_user_shows,
				container, false);

		showId = getArguments().getInt(DetailUserShowFragment.USER_SHOW_INFO);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
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
			editor.putInt(STATE_USER_SHOW_ID, userShow.getId());
			editor.putBoolean(STATE_USER_SHOW_IS_BOUGHT, userShow.isBought());
			editor.putString(STATE_USER_SHOW_MOVIE_NAME,
					userShow.getMovieTitle());
			editor.putString(STATE_USER_SHOW_TIME,
					userShow.getShowDateAndTime());
			editor.putString(STATE_USER_SHOW_COMPLEX_ADDRESS,
					userShow.getComplexAddress());
			editor.putString(STATE_USER_SHOW_QR_STRING, userShow.getQRString());
			editor.putInt(STATE_USER_SHOW_PRICE, userShow.getShowPrice(false));

			editor.commit();
		}
	}

	private void loadDetailUserShow() {
		SharedPreferences preferences = getActivity().getPreferences(0);
		int invalidId = -1;
		int id = preferences.getInt(STATE_USER_SHOW_ID, invalidId);

		if (invalidId != id) {
			doLoadDetailUserShow();
		}
	}

	private void doLoadDetailUserShow() {
		int id, price;
		boolean isBought;
		String movieName, showTime, complexAddress, qrstring;

		SharedPreferences preferences = getActivity().getPreferences(0);
		id = preferences.getInt(STATE_USER_SHOW_ID, 0);
		price = preferences.getInt(STATE_USER_SHOW_PRICE, 0);
		isBought = preferences.getBoolean(STATE_USER_SHOW_IS_BOUGHT, false);
		movieName = preferences.getString(STATE_USER_SHOW_MOVIE_NAME, "-");
		showTime = preferences.getString(STATE_USER_SHOW_TIME, "-");
		complexAddress = preferences.getString(STATE_USER_SHOW_COMPLEX_ADDRESS,
				"-");
		qrstring = preferences.getString(STATE_USER_SHOW_QR_STRING, "-");

		userShow = new DetailUserShow(id, isBought, movieName, showTime,
				complexAddress, qrstring, price);
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
		startActivity(intent);
	}

	protected void onCancelReservationAction() {
		ConfirmShowReserveCancelationDialogFragment dialog = new ConfirmShowReserveCancelationDialogFragment();
		dialog.show(getFragmentManager(), "dialog.confirmation");
	}

	private void populateUserShowView() {
		shareButtonsVisibilityListener.showFacebookShareButton();
		shareButtonsVisibilityListener.showTwitterShareButton();
		shareButtonsVisibilityListener.showCalendarButton();
		shareActionListener.shareTextonFacebook("Me voy a ver "
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
		showPriceTextView.setText("Precio final: $" + userShow.getShowPrice(true));
		placesCountTextView.setText(userShow.getSeats().size() + " Lugar"
				+ ((1 != userShow.getSeats().size()) ? "es" : ""));
		discountsTextView.setText("discounts");
		placesCodesTextView.setText("places codes");

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

}
