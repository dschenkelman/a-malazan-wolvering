package phoneticket.android.activities.fragments;

import java.util.Collection;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.adapter.ExpandableMovieFunctionsAdapter;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.IMovieFunctions;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import phoneticket.android.utils.ImageDownloader;
import roboguice.fragment.RoboFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailMovieFragment extends RoboFragment implements
		IRetrieveMovieInfoServiceDelegate,
		IRetrieveMovieFunctionsServiceDelegate {

	public static final String EXTRA_MOVIE_ID = "bundle.detailmovie.id";

	@Inject
	private IRetrieveMovieInfoService movieInfoService;

	@Inject
	private IRetrieveMovieFunctionsService movieFunctionsService;

	private int movieId;
	private IMovie movie;

	private ExpandableListView expandableList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_detail_movie, container,
				false);
		Button mockButton = (Button) view.findViewById(R.id.watchTrailerButton);
		mockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onWatchTrailerButtonAction(v);
			}
		});

		movieId = getArguments().getInt(DetailMovieFragment.EXTRA_MOVIE_ID);
		retrieveMovieInfoAction();
		retrieveMovieFunctionsAction();

		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loadingDataLayout);
		LinearLayout dataLayout = (LinearLayout) view
				.findViewById(R.id.dataLayout);
		loadingLayout.setVisibility(LinearLayout.VISIBLE);
		dataLayout.setVisibility(LinearLayout.GONE);

		Button refeshButton = (Button) loadingLayout
				.findViewById(R.id.refreshDataButton);
		refeshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retrieveMovieInfoAction();
			}
		});

		expandableList = (ExpandableListView) view
				.findViewById(R.id.expandableFunctionList);

		return view;
	}

	private void retrieveMovieFunctionsAction() {
		movieFunctionsService.retrieveMovieFunctions(this, movieId);

	}

	protected void retrieveMovieInfoAction() {
		movieInfoService.retrieveMovieInfo(this, movieId);
	}

	private void showMovie() {
		TextView title = (TextView) getView().findViewById(R.id.movieTitleText);
		TextView gendre = (TextView) getView().findViewById(
				R.id.movieGendreText);
		TextView duration = (TextView) getView().findViewById(
				R.id.movieDurationText);
		TextView clasification = (TextView) getView().findViewById(
				R.id.movieClasificationText);
		TextView synopsis = (TextView) getView().findViewById(
				R.id.movieSynopsisText);

		String gendreString = getResources().getString(
				R.string.detailMovieGendreStartText)
				+ movie.getGenre();
		String durationString = getResources().getString(
				R.string.detailMovieDurationStartText)
				+ movie.getDurationInMinutes()
				+ getResources().getString(R.string.detailMovieDurationEndText);
		String clasificationString = getResources().getString(
				R.string.detailMovieClasificationStartText)
				+ movie.getClasification();

		title.setText(movie.getTitle());
		gendre.setText(gendreString);
		duration.setText(durationString);
		clasification.setText(clasificationString);
		synopsis.setText(movie.getSynopsis());

		ImageView poster = (ImageView) getView().findViewById(R.id.movieImage);
		ImageDownloader downloader = new ImageDownloader(movie.getImageURL(),
				poster, null, null);
		downloader.execute();
	}

	private void showProgressLayout(boolean refreshButtonVisible) {
		RelativeLayout loadingLayout = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		Button refeshButton = (Button) loadingLayout
				.findViewById(R.id.refreshDataButton);
		LinearLayout dataLayout = (LinearLayout) getView().findViewById(
				R.id.dataLayout);
		loadingLayout.setVisibility(LinearLayout.VISIBLE);
		refeshButton.setVisibility(refreshButtonVisible ? Button.VISIBLE
				: Button.GONE);
		dataLayout.setVisibility(LinearLayout.GONE);
	}

	private void hideProgressLayout() {
		RelativeLayout loadingLayout = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		Button refeshButton = (Button) loadingLayout
				.findViewById(R.id.refreshDataButton);
		LinearLayout dataLayout = (LinearLayout) getView().findViewById(
				R.id.dataLayout);
		loadingLayout.setVisibility(LinearLayout.GONE);
		refeshButton.setVisibility(Button.GONE);
		dataLayout.setVisibility(LinearLayout.VISIBLE);
	}

	public void onWatchTrailerButtonAction(View sender) {
		if (movie != null) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(movie.getTrailerUrl()));
			startActivity(browserIntent);
		}
	}

	@Override
	public void retrieveMovieInfoFinish(IRetrieveMovieInfoService service,
			IMovie movie) {
		this.movie = movie;
		showMovie();
		hideProgressLayout();
	}

	@Override
	public void retrieveMovieInfoFinishWithError(
			IRetrieveMovieInfoService service, Integer errorCode) {
		showProgressLayout(true);
	}

	@Override
	public void retrieveMovieFunctionsFinish(
			IRetrieveMovieFunctionsService service,
			Collection<IMovieFunctions> movieFunctions) {
		expandableList.setAdapter(new ExpandableMovieFunctionsAdapter(
				getActivity(), movieFunctions));
	}

	@Override
	public void retrieveMovieFunctionsFinishWithError(
			IRetrieveMovieFunctionsService service, Integer errorCode) {

	}
}
