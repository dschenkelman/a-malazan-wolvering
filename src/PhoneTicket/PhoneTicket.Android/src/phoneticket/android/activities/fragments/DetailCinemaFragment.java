package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.IRetrieveCinemaInfoService;
import phoneticket.android.services.get.IRetrieveCinemaInfoServiceDelegate;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import roboguice.fragment.RoboFragment;

public class DetailCinemaFragment extends RoboFragment implements
		IRetrieveCinemaInfoServiceDelegate {

	public static final String EXTRA_CINEMA_ID = "bundle.detailcinema.id";

	@Inject
	private IRetrieveCinemaInfoService cinemaInfoService;

	private ICinema cinema;

	private boolean ignoreServicesCallbacks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_detail_cinema,
				container, false);
		Button button = (Button) fragment.findViewById(R.id.refreshViewButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRefreshCinemasInfoAction();
			}

		});
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		int cinemaId = getArguments().getInt(
				DetailCinemaFragment.EXTRA_CINEMA_ID);
		cinemaInfoService.retrieveCinemaInfo(this, cinemaId);
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	private void onRefreshCinemasInfoAction() {
		showProgressLayout();
		this.cinemaInfoService.retrieveCinemaInfo(this, this.cinema.getId());
	}

	@Override
	public void retrieveCinemaInfoFinish(IRetrieveCinemaInfoService service,
			ICinema cinema) {
		if (!ignoreServicesCallbacks) {
			hideProgressLayout();
			this.cinema = cinema;
			((TextView) getView().findViewById(R.id.cinemaName))
					.setText(this.cinema.getName());
			((TextView) getView().findViewById(R.id.cinemaAddress))
					.setText(this.cinema.getAddress());
		}
	}

	@Override
	public void retrieveCinemaInfoFinishWithError(
			IRetrieveCinemaInfoService service, Integer errorCode) {
		if (!ignoreServicesCallbacks)
			hideProgressLayoutWithError();
	}

	private void showProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		LinearLayout dataLayout = (LinearLayout) getActivity().findViewById(
				R.id.cinemaInfo);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(LinearLayout.GONE);
		loadingLayout.setVisibility(RelativeLayout.VISIBLE);
	}

	private void hideProgressLayoutWithError() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		LinearLayout dataLayout = (LinearLayout) getActivity().findViewById(
				R.id.cinemaInfo);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.VISIBLE);
		dataLayout.setVisibility(LinearLayout.GONE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	private void hideProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		LinearLayout dataLayout = (LinearLayout) getActivity().findViewById(
				R.id.cinemaInfo);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(LinearLayout.VISIBLE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}
}
