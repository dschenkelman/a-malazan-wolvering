package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.adapter.TimeFunctionAdapter;
import phoneticket.android.model.IFunction;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.IMovieFunctions;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import phoneticket.android.utils.ImageDownloader;
import roboguice.fragment.RoboFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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

	private LinearLayout functionsLayout;

	private ArrayList<String> expandedGroupsIds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		expandedGroupsIds = new ArrayList<String>();

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

		functionsLayout = (LinearLayout) view
				.findViewById(R.id.functionsLayout);

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
			final Collection<IMovieFunctions> moviesFunctions) {

		functionsLayout.removeAllViews();
		int index = 0;
		for (IMovieFunctions movieFunctions : moviesFunctions) {

			// layout group container
			LinearLayout groupLayoutView = new LinearLayout(getActivity());
			groupLayoutView.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams();
			layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			groupLayoutView.setLayoutParams(layoutParams);

			// header group view
			LayoutInflater infalInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View headerView = infalInflater.inflate(
					R.layout.exp_moviefuncts_header_group, null);
			TextView titleTextView = (TextView) headerView
					.findViewById(R.id.titleTextView);
			titleTextView.setTypeface(null, Typeface.BOLD);
			titleTextView.setText(movieFunctions.getCinemaName());
			if (index + 1 < moviesFunctions.size()) {
				View removableLine = (View) headerView
						.findViewById(R.id.removableLine);
				removableLine.setVisibility(View.GONE);
			}
			groupLayoutView.addView(headerView);

			// calculating days
			ArrayList<String> days = new ArrayList<String>();
			for (IFunction function : movieFunctions.getFunctions()) {
				if (false == days.contains(function.getDay())) {
					days.add(function.getDay());
				}
			}

			// child group views
			final Collection<View> childViews = new ArrayList<View>();
			for (String day : days) {
				// getting child functions
				Collection<IFunction> dayFunctions = new ArrayList<IFunction>();
				for (IFunction function : movieFunctions.getFunctions()) {
					if (function.getDay().equals(day)) {
						dayFunctions.add(function);
					}
				}

				// creating the child group view for the day
				View dayView = infalInflater.inflate(
						R.layout.exp_moviefuncts_child, null);
				TextView txtListChild = (TextView) dayView
						.findViewById(R.id.childTextView);
				GridView gridView = (GridView) dayView
						.findViewById(R.id.timeGrid);
				gridView.setAdapter(new TimeFunctionAdapter(getActivity(),
						dayFunctions));
				txtListChild.setText(day);
				groupLayoutView.addView(dayView);
				dayView.setVisibility(View.GONE);
				if (days.indexOf(day) == (days.size() - 1)) {
					View removableLine = (View) dayView
							.findViewById(R.id.removableLine);
					removableLine.setVisibility(View.GONE);
				}
				childViews.add(dayView);
			}

			// header listeners
			final int headerIndex = index;
			ImageButton expandContract = (ImageButton) headerView
					.findViewById(R.id.expandContractButton);
			expandContract.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isExpanded = false;
					String id = headerIndex + ".id";
					if (expandedGroupsIds.contains(id)) {
						expandedGroupsIds.remove(id);
						isExpanded = false;
					} else {
						expandedGroupsIds.add(id);
						isExpanded = true;
					}
					for (View child : childViews) {
						child.setVisibility(isExpanded ? View.VISIBLE
								: View.GONE);
					}
					if (headerIndex + 1 < moviesFunctions.size()) {
						View removableLine = (View) headerView
								.findViewById(R.id.removableLine);
						removableLine.setVisibility(isExpanded ? View.VISIBLE
								: View.GONE);
					}
				}
			});

			//
			functionsLayout.addView(groupLayoutView);
			index++;
		}
	}

	@Override
	public void retrieveMovieFunctionsFinishWithError(
			IRetrieveMovieFunctionsService service, Integer errorCode) {

	}
}
