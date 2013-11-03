package phoneticket.android.activities.fragments;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IRibbonChangeMenuListener;
import phoneticket.android.activities.interfaces.IShareActionListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.model.Cinema;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;
import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CinemasFragment extends RoboFragment implements
		IRetrieveCinemaListServiceDelegate {

	private static final String STATE_CINEMAS_STREAM = "state.cinemas.stream";
	private static final String STATE_CINEMAS_SEPARATOR_ITEMS = "]";
	private static final String STATE_CINEMAS_SEPARATOR_PROPERTIES = "#";

	@Inject
	private IRetrieveCinemaListService cinemaListService;
	private boolean ignoreServicesCallbacks;
	private List<ICinema> cinemas;
	private IShareButtonsVisibilityListener shareButonVisibilityListener;
	private IShareActionListener shareActionListener;
	private IRibbonChangeMenuListener ribbonListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_cinemas, container,
				false);

		Button button = (Button) fragment.findViewById(R.id.refreshViewButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRefreshCinemasListAction();
			}
		});
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		ribbonListener.setOnCinemasMenu();
		ignoreServicesCallbacks = false;

		shareButonVisibilityListener.hideFacebookShareButton();
		shareButonVisibilityListener.hideTwitterShareButton();

		SharedPreferences preferences = getActivity().getPreferences(0);
		String cinemasStream = preferences.getString(STATE_CINEMAS_STREAM, "");
		boolean recreatingState = 0 != cinemasStream.length();

		if (recreatingState) {
			Collection<ICinema> storedCinemas = createCinemasFromStream(cinemasStream);
			if (0 != storedCinemas.size()) {
				createCinemasView(storedCinemas);
			} else {
				cinemaListService.retrieveCinemaList(this);
			}
		} else {
			cinemaListService.retrieveCinemaList(this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;

		if (null != cinemas) {
			String cinemasStream = "";
			for (ICinema cinemaItem : cinemas) {
				cinemasStream += cinemaItem.getId()
						+ STATE_CINEMAS_SEPARATOR_PROPERTIES
						+ cinemaItem.getName()
						+ STATE_CINEMAS_SEPARATOR_PROPERTIES
						+ cinemaItem.getAddress()
						+ STATE_CINEMAS_SEPARATOR_PROPERTIES
						+ cinemaItem.getLocation().toString()
						+ STATE_CINEMAS_SEPARATOR_ITEMS;
			}
			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.putString(STATE_CINEMAS_STREAM, cinemasStream);
			editor.commit();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		deleteSavedCinemasData();
	}

	private void deleteSavedCinemasData() {
		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();
		editor.remove(STATE_CINEMAS_STREAM);
		editor.commit();
	}

	private List<ICinema> createCinemasFromStream(String cinemasStream) {
		List<ICinema> cinemas = new LinkedList<ICinema>();
		try {
			String items[] = cinemasStream.split(STATE_CINEMAS_SEPARATOR_ITEMS);
			for (String itemStream : items) {
				String values[] = itemStream
						.split(STATE_CINEMAS_SEPARATOR_PROPERTIES);
				int id = 0;
				String name = "", address = "", location = "";
				if (0 < values.length)
					id = Integer.parseInt(values[0]);
				if (1 < values.length)
					name = values[1];
				if (2 < values.length)
					address = values[2];
				if (3 < values.length)
					location = values[3];
				Cinema item = new Cinema(id, name, address);
				item.setStringLocation(location);
				cinemas.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			deleteSavedCinemasData();
			cinemas = new LinkedList<ICinema>();
		}
		return cinemas;
	}

	private void createCinemasView(Collection<ICinema> cinemas) {
		LinearLayout cinemaListView = (LinearLayout) getView().findViewById(
				R.id.cinemaList);
		cinemaListView.removeAllViews();

		this.cinemas = new LinkedList<ICinema>();
		this.cinemas.addAll(cinemas);

		for (final ICinema cinema : this.cinemas) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			LinearLayout row = (LinearLayout) inflater.inflate(
					R.layout.row_cinema_list, cinemaListView, false);

			TextView nameTextView = (TextView) row
					.findViewById(R.id.cinemaName);
			TextView addressTextView = (TextView) row
					.findViewById(R.id.cinemaAddress);
			Button goToMapButton = (Button) row
					.findViewById(R.id.showInGoogleMaps);
			ImageButton twitterButton = (ImageButton) row
					.findViewById(R.id.twitterButton);
			ImageButton facebookButton = (ImageButton) row
					.findViewById(R.id.facebookButton);

			nameTextView.setText(cinema.getName());
			addressTextView.setText(cinema.getAddress());
			goToMapButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (null != cinema.getLocation()) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse("geo:" + cinema.getLatitude() + ","
										+ cinema.getLongitude() + "?q="
										+ cinema.getLatitude() + ","
										+ cinema.getLongitude() + "&z=17"));
						startActivity(intent);
					}
				}
			});
			twitterButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String twitterMessage = "Voy al complejo "
							+ cinema.getName()
							+ " de CINEMAR. Visita www.cinemar.com.ar";
					shareActionListener.shareOnTwitter(twitterMessage);
				}
			});
			facebookButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					shareActionListener.shareCinemaOnFacebook(
							cinema.getLatitude(), cinema.getLongitude(),
							cinema.getAddress(), cinema.getName());
				}
			});

			cinemaListView.addView(row);
		}
		hideProgressLayout();
	}

	@Override
	public void retrieveCinemaListFinish(IRetrieveCinemaListService service,
			Collection<ICinema> cinemas) {
		if (!ignoreServicesCallbacks) {
			createCinemasView(cinemas);
		}
	}

	public void onRefreshCinemasListAction() {
		showProgressLayout();
		cinemaListService.retrieveCinemaList(this);
	}

	@Override
	public void retrieveCinemaListFinishWithError(
			IRetrieveCinemaListService service, Integer errorCode) {
		if (!ignoreServicesCallbacks)
			hideProgressLayoutWithError();
	}

	private void showProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		LinearLayout dataLayout = (LinearLayout) getActivity().findViewById(
				R.id.cinemaList);
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
				R.id.cinemaList);
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
				R.id.cinemaList);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(LinearLayout.VISIBLE);

		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			shareButonVisibilityListener = (IShareButtonsVisibilityListener) activity;
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
			ribbonListener = (IRibbonChangeMenuListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IRibbonChangeMenuListener");
		}
	}
}
