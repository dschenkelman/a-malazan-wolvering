package phoneticket.android.activities.fragments;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IOnCinemaSelectedListener;
import phoneticket.android.adapter.CinemaAdapter;
import phoneticket.android.model.ICinema;
import phoneticket.android.services.get.IRetrieveCinemaListService;
import phoneticket.android.services.get.IRetrieveCinemaListServiceDelegate;
import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class CinemasFragment extends RoboFragment implements
		IRetrieveCinemaListServiceDelegate {

	@Inject
	private IRetrieveCinemaListService cinemaListService;

	private boolean ignoreServicesCallbacks;

	private IOnCinemaSelectedListener cinemaListItemSelectedListener;

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
		ignoreServicesCallbacks = false;
		cinemaListService.retrieveCinemaList(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void retrieveCinemaListFinish(IRetrieveCinemaListService service,
			Collection<ICinema> cinemas) {
		if (!ignoreServicesCallbacks) {
			ListView cinemaListView = (ListView) getView().findViewById(
					R.id.cinemaList);

			List<ICinema> cinemaList = new LinkedList<ICinema>();
			cinemaList.addAll(cinemas);
			final CinemaAdapter adapter = new CinemaAdapter(getActivity(),
					R.id.cinemaInfo, cinemaList);
			cinemaListView.setAdapter(adapter);
			cinemaListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					ICinema selectedCinema = adapter.getItem(position);
					cinemaListItemSelectedListener.onCinemaSelected(
							selectedCinema.getId(), selectedCinema.getName());
				}
			});
			hideProgressLayout();
			adapter.notifyDataSetChanged();
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
		ListView dataLayout = (ListView) getActivity().findViewById(
				R.id.cinemaList);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(ListView.GONE);
		loadingLayout.setVisibility(RelativeLayout.VISIBLE);
	}

	private void hideProgressLayoutWithError() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		ListView dataLayout = (ListView) getActivity().findViewById(
				R.id.cinemaList);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.VISIBLE);
		dataLayout.setVisibility(ListView.GONE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	private void hideProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		ListView dataLayout = (ListView) getActivity().findViewById(
				R.id.cinemaList);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(ListView.VISIBLE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			cinemaListItemSelectedListener = (IOnCinemaSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IOnCinemaListItemSelectedListener");
		}
	}
}
