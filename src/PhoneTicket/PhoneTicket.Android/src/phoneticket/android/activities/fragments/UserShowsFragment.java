package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.MasterActivity.IOnUserShowChangesListener;
import phoneticket.android.activities.interfaces.IDetailUserShowListener;
import phoneticket.android.model.IDetailUserShow;
import phoneticket.android.model.IMyShow;
import phoneticket.android.model.MyShow;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
		IRetrieveMyShowsServiceDelegate, IOnUserShowChangesListener {

	private static final String STATE_SHOWS_STREAM = "state.usershows.stream";
	private static final String STATE_SHOWS_SEPARATOR_ITEMS = "]";
	private static final String STATE_SHOWS_SEPARATOR_PROPERTIES = "#";
	
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
		saveMyShows();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		deleteSavedShowsData();
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

	private void saveMyShows() {
		if (null != myShows) {
			String showsStream = "";
			for (IMyShow cinemaItem : myShows) {
				showsStream += cinemaItem.getId()
						+ STATE_SHOWS_SEPARATOR_PROPERTIES
						+ cinemaItem.isBought()
						+ STATE_SHOWS_SEPARATOR_PROPERTIES
						+ cinemaItem.getMovieTitle()
						+ STATE_SHOWS_SEPARATOR_PROPERTIES
						+ cinemaItem.getShowDateAndTime().toString()
						+ STATE_SHOWS_SEPARATOR_PROPERTIES
						+ cinemaItem.getComplexAddress().toString()
						+ STATE_SHOWS_SEPARATOR_ITEMS;
			}
			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.putString(STATE_SHOWS_STREAM, showsStream);
			editor.commit();
		}
	}

	private void getMyShows() {
		myShows = new ArrayList<IMyShow>();
		
		SharedPreferences preferences = getActivity().getPreferences(0);
		String showsStream = preferences.getString(STATE_SHOWS_STREAM, "");
		boolean shouldRecreateState = 0 != showsStream.length();

		if (shouldRecreateState) {
			myShows.addAll(createShowsFromStream(showsStream));
		}
	}

	private void deleteSavedShowsData() {
		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();
		editor.remove(STATE_SHOWS_STREAM);
		editor.commit();
	}

	private List<IMyShow> createShowsFromStream(String showsStream) {
		List<IMyShow> shows = new LinkedList<IMyShow>();
		try {
			String items[] = showsStream.split(STATE_SHOWS_SEPARATOR_ITEMS);
			for (String itemStream : items) {
				String values[] = itemStream
						.split(STATE_SHOWS_SEPARATOR_PROPERTIES);
				String id = "a000aaaa-a000-a000-a0a0-a00a0aaa0a00";
				boolean isBought = true;
				String movieName = "";
				String showTime = "";
				String complexAddress = "";
				if (0 < values.length)
					id = values[0];
				if (1 < values.length)
					isBought = Boolean.parseBoolean(values[1]);
				if (2 < values.length)
					movieName = values[2];
				if (3 < values.length)
					showTime = values[3];
				if (4 < values.length)
					complexAddress = values[4];
				
				MyShow item = new MyShow(id, isBought, movieName, showTime, complexAddress);
				shows.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			deleteSavedShowsData();
			shows = new LinkedList<IMyShow>();
		}
		return shows;
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
			return position;
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

			LinearLayout linear = (LinearLayout) rowView
					.findViewById(R.id.textLayout);

			TextView moviewNameTextView = (TextView) linear
					.findViewById(R.id.userShowMovieName);
			TextView showTimeTextView = (TextView) linear
					.findViewById(R.id.userShowShowTime);
			TextView addressTextView = (TextView) linear
					.findViewById(R.id.userShowCinemaAddress);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.userShowIcon);

			moviewNameTextView.setText(myShow.getMovieTitle());
			showTimeTextView.setText(myShow.getShowDateAndTime());
			addressTextView.setText(myShow.getComplexAddress());

			if (myShow.isBought()) {
				imageView.setImageResource(R.drawable.ic_user_show_bought);
			} else {
				imageView.setImageResource(R.drawable.ic_user_show_reserved);
			}

			return rowView;
		}
	}

	@Override
	public void userShowCanceled(IDetailUserShow userShow) {
		IMyShow show = null;
		for (IMyShow myShow : myShows) {
			if (myShow.getId() == userShow.getId()) {
				show = myShow;
				break;
			}
		}
		if (null != show) {
			myShows.remove(show);
			saveMyShows();
			
			ListView myShowsList = (ListView) getView().findViewById(
					R.id.myShowsListView);
			((MyShowsAdapter)myShowsList.getAdapter()).remove(show);
		}
	}
}
