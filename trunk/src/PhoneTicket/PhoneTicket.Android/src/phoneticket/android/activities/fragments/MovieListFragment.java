package phoneticket.android.activities.fragments;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IOnMovielistItemSelectedListener;
import phoneticket.android.activities.interfaces.IShareButtonsVisibilityListener;
import phoneticket.android.adapter.ImageAdapter;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.model.MovieListItem;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;
import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MovieListFragment extends RoboFragment implements
		IRetrieveMovieListServiceDelegate {

	private static final String STATE_MOVIELIST_STREAM = "state.movielist.stream";

	@Inject
	private IRetrieveMovieListService movieListService;

	private IOnMovielistItemSelectedListener movielistSlectionListener;
	private IShareButtonsVisibilityListener shareButonVisibilityListener;

	private boolean ignoreServicesCallbacks;
	private List<IMovieListItem> movies;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_movielist,
				container, false);
		Button button = (Button) fragment.findViewById(R.id.refreshViewButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onRefreshMovieListAction();

			}
		});
		ignoreServicesCallbacks = false;
		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;

		shareButonVisibilityListener.hideFacebookShareButton();
		shareButonVisibilityListener.hideTwitterShareButton();

		SharedPreferences preferences = getActivity().getPreferences(0);
		String movielistStream = preferences.getString(STATE_MOVIELIST_STREAM,
				"");
		boolean recreatingState = 0 != movielistStream.length();

		if (recreatingState) {
			createMovielistView(createMovielistFromStream(movielistStream));
		} else {
			movieListService.retrieveMovieList(this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;

		if (null != movies) {

			String movieListStream = "";
			for (IMovieListItem movieListItem : movies) {
				movieListStream += movieListItem.getId() + "#"
						+ movieListItem.getTitle() + "#"
						+ movieListItem.getImageURL() + "]";
			}
			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.putString(STATE_MOVIELIST_STREAM, movieListStream);
			editor.commit();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();
		editor.remove(STATE_MOVIELIST_STREAM);
		editor.commit();
	}

	private List<IMovieListItem> createMovielistFromStream(
			String movielistStream) {
		List<IMovieListItem> movielist = new LinkedList<IMovieListItem>();
		String items[] = movielistStream.split("]");
		for (String itemStream : items) {
			String values[] = itemStream.split("#");
			int id = 0;
			String title = "", imageUrl = "";

			if (0 < values.length)
				id = Integer.parseInt(values[0]);
			if (1 < values.length)
				title = values[1];
			if (2 < values.length)
				imageUrl = values[2];
			MovieListItem item = new MovieListItem(id, title, imageUrl);
			movielist.add(item);
		}
		return movielist;
	}

	private void createMovielistView(Collection<IMovieListItem> movieList) {
		GridView gridview = (GridView) getView().findViewById(R.id.gridview);
		movies = new LinkedList<IMovieListItem>();
		movies.addAll(movieList);
		final ImageAdapter imageAdapter = new ImageAdapter(getActivity(),
				R.id.scaleImageView, movies);
		gridview.setAdapter(imageAdapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				IMovieListItem selectedMovie = imageAdapter.getItem(position);
				movielistSlectionListener.onMovielistItemSelected(
						selectedMovie.getId(), selectedMovie.getTitle());
			}
		});
		hideProgressLayout();
		imageAdapter.notifyDataSetChanged();
	}

	@Override
	public void retrieveMovieListFinish(IRetrieveMovieListService service,
			Collection<IMovieListItem> movieList) {
		if (!ignoreServicesCallbacks) {
			createMovielistView(movieList);
		}
	}

	@Override
	public void retrieveMovieListFinishWithError(
			IRetrieveMovieListService service, Integer errorCode) {
		if (!ignoreServicesCallbacks)
			hideProgressLayoutWithError();
	}

	private void showProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		GridView dataLayout = (GridView) getActivity().findViewById(
				R.id.gridview);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(GridView.GONE);
		loadingLayout.setVisibility(RelativeLayout.VISIBLE);
	}

	private void hideProgressLayoutWithError() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		GridView dataLayout = (GridView) getActivity().findViewById(
				R.id.gridview);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.VISIBLE);
		dataLayout.setVisibility(GridView.GONE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	private void hideProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getActivity()
				.findViewById(R.id.loadingDataLayout);
		GridView dataLayout = (GridView) getActivity().findViewById(
				R.id.gridview);
		RelativeLayout errorContainer = (RelativeLayout) getActivity()
				.findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(RelativeLayout.GONE);
		dataLayout.setVisibility(GridView.VISIBLE);
		loadingLayout.setVisibility(RelativeLayout.GONE);
	}

	public void onRefreshMovieListAction() {
		if (!ignoreServicesCallbacks) {
			movieListService.retrieveMovieList(this);
			showProgressLayout();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			movielistSlectionListener = (IOnMovielistItemSelectedListener) activity;
			shareButonVisibilityListener = (IShareButtonsVisibilityListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IOnMovielistItemSelectedListener");
		}
	}
}
