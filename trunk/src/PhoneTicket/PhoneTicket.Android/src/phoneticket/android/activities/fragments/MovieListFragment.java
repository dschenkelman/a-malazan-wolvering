package phoneticket.android.activities.fragments;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.adapter.ImageAdapter;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;
import roboguice.fragment.RoboFragment;
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

	@Inject
	private IRetrieveMovieListService movieListService;

	private IFragmentChange activity;

	private boolean ignoreServicesCallbacks;

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
		activity = (IFragmentChange) getActivity();
		ignoreServicesCallbacks = false;
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		movieListService.retrieveMovieList(this);
	}

	@Override
	public void retrieveMovieListFinish(IRetrieveMovieListService service,
			Collection<IMovieListItem> movieList) {
		if (!ignoreServicesCallbacks) {
			GridView gridview = (GridView) getView()
					.findViewById(R.id.gridview);
			List<IMovieListItem> movies = new LinkedList<IMovieListItem>();
			movies.addAll(movieList);
			final ImageAdapter imageAdapter = new ImageAdapter(getActivity(),
					R.id.scaleImageView, movies);
			gridview.setAdapter(imageAdapter);

			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Bundle movieData = new Bundle();
					movieData.putInt(DetailMovieFragment.EXTRA_MOVIE_ID,
							imageAdapter.getItem(position).getId());
					DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
					detailMovieFragment.setArguments(movieData);
					activity.changeFragment(detailMovieFragment);

				}
			});
			hideProgressLayout();
			imageAdapter.notifyDataSetChanged();
		}
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

}
