package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.ConfirmShowReserveCancelationDialogFragment;
import phoneticket.android.activities.dialog.ConfirmShowReserveCancelationDialogFragment.IConfirmShowReserveCancelationDialogDelegate;
import phoneticket.android.activities.interfaces.IShareActionListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.activities.interfaces.IUserShowsActionListener;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.services.get.IRetrieveUserShowInfoService;
import phoneticket.android.services.get.IRetrieveUserShowInfoServiceDelegate;
import phoneticket.android.services.post.ICancelUserShowService;
import phoneticket.android.services.post.ICancelUserShowServiceDelegate;
import android.app.Activity;
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
	}

	@Override
	public void onDetach() {
		super.onDetach();
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

	private boolean shouldRetrieveUserShow() {
		// TODO Auto-generated method stub
		return true;
	}

	private void onRetrieveUserShowAction() {
		showLoadingLayout();
		infoService.retrieveUserShowInfo(this, showId);
	}

	protected void onGetQRCodeAction() {
		// TODO Auto-generated method stub

	}

	protected void onBuyReservationAction() {
		// TODO Auto-generated method stub

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
				+ userShow.getMovieName() + " a CINEMAR");

		TextView nameAndTimeTextView = (TextView) getView().findViewById(
				R.id.movieNameAndShowTimeTextView);
		TextView cinemaAddressTextView = (TextView) getView().findViewById(
				R.id.complexAddressTextView);
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

		nameAndTimeTextView.setText(userShow.getMovieName() + " - "
				+ userShow.getShowTime());
		cinemaAddressTextView.setText(userShow.getComplexAddress());
		placesCountTextView.setText(userShow.getTicketsCount() + " Lugar"
				+ ((1 != userShow.getTicketsCount()) ? "es" : ""));
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
		TextView message = (TextView)((RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout)).findViewById(R.id.downloadingDataTextView);
		message.setText("Cargando los datos. Por Favor, espere un momento.");
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ScrollView.GONE);
	}

	private void showErrorLayout() {
		layoutVisibility(LinearLayout.VISIBLE, RelativeLayout.GONE,
				ScrollView.GONE);
	}

	private void showCancelingLayout() {
		TextView message = (TextView)((RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout)).findViewById(R.id.downloadingDataTextView);
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
			// TODO show an error dialog
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
