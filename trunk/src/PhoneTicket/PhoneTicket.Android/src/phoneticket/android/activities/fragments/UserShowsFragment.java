package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IDetailUserShowListener;
import phoneticket.android.model.IMyShow;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import roboguice.fragment.RoboFragment;

public class UserShowsFragment extends RoboFragment implements
		IRetrieveMyShowsServiceDelegate {

	private boolean ignoreServicesCallbacks;

	@Inject
	private IRetrieveMyShowsService myShowsService;

	private Collection<IMyShow> myShows;

	private IDetailUserShowListener detailListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_shows, container,
				false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;

		getMyShows();

		if (shouldRetrieveMyShows()) {
			onRetrieveMyShowsAction();
		} else {
			createMyShowsLayout();
			showMyShowsListLayout();
		}

		((Button) getView().findViewById(R.id.reloadDataButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onRetrieveMyShowsAction();
					}
				});
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			detailListener = (IDetailUserShowListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IDetailUserShowListener");
		}
	}

	private void getMyShows() {
		myShows = new ArrayList<IMyShow>();
		// TODO load shows from disk
	}

	private boolean shouldRetrieveMyShows() {
		return (null == myShows) || (0 == myShows.size());
	}

	protected void onRetrieveMyShowsAction() {
		showLoadingLayout();
		myShowsService.retrieveMyShows(this);
	}

	private void showMyShowsListLayout() {
		layoutVisibility(LinearLayout.GONE, RelativeLayout.GONE,
				ListView.VISIBLE);
	}

	private void showLoadingLayout() {
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ListView.GONE);
	}

	private void showErrorLayout() {
		layoutVisibility(LinearLayout.VISIBLE, RelativeLayout.GONE,
				ListView.GONE);
	}

	private void layoutVisibility(int erroVisibility, int loadingVisibility,
			int myShowsVisibilty) {
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ListView myShows = (ListView) getView().findViewById(
				R.id.myShowsListView);

		errorView.setVisibility(erroVisibility);
		loadingView.setVisibility(loadingVisibility);
		myShows.setVisibility(myShowsVisibilty);
	}

	private void createMyShowsLayout() {
		ListView myShowsList = (ListView) getView().findViewById(
				R.id.myShowsListView);

		MyShowsAdapter myShowsAdapater = null;
		if (null == myShowsList.getAdapter()) {
			myShowsAdapater = new MyShowsAdapter(getActivity(), getId());

			myShowsList.setAdapter(myShowsAdapater);
		} else {
			myShowsAdapater = (MyShowsAdapter) myShowsList.getAdapter();
		}

		myShowsAdapater.clear();
		myShowsAdapater.addAll(myShows);

		myShowsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				detailListener.onShowDetailUserShowAction((IMyShow) (myShows
						.toArray()[position]));
			}
		});
	}

	@Override
	public void retrieveMyShowsServiceFinished(IRetrieveMyShowsService service,
			Collection<IMyShow> myShows) {
		if (false == ignoreServicesCallbacks) {
			this.myShows = myShows;
			createMyShowsLayout();
			showMyShowsListLayout();
		}
	}

	@Override
	public void retrieveMyShowsServiceFinishedWithError(
			IRetrieveMyShowsService service, int errorCode) {
		if (false == ignoreServicesCallbacks) {
			showErrorLayout();
		}
	}

	private class MyShowsAdapter extends ArrayAdapter<IMyShow> {

		private final Context context;

		public MyShowsAdapter(Context context, int resource) {
			super(context, resource);
			this.context = context;
		}

		@Override
		public long getItemId(int position) {
			return ((IMyShow) (myShows.toArray()[position])).getId();
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			IMyShow myShow = (IMyShow) (myShows.toArray()[position]);

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_user_show, parent,
					false);

			TextView moviewNameTextView = (TextView) rowView
					.findViewById(R.id.userShowMovieName);
			TextView showTimeTextView = (TextView) rowView
					.findViewById(R.id.userShowShowTime);
			TextView addressTextView = (TextView) rowView
					.findViewById(R.id.userShowCinemaAddress);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.userShowIcon);

			moviewNameTextView.setText(myShow.getMovieName());
			showTimeTextView.setText(myShow.getShowTime());
			addressTextView.setText(myShow.getComplexAddress());

			if (myShow.isBought()) {
				imageView.setImageResource(R.drawable.ic_user_show_bought);
			} else {
				imageView.setImageResource(R.drawable.ic_user_show_reserved);
			}

			return rowView;
		}
	}
}
