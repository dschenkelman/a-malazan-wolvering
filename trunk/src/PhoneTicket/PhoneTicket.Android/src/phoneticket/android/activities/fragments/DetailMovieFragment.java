package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.adapter.TimeFunctionAdapter;
import phoneticket.android.model.IFunction;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.IMovieFunctions;
import phoneticket.android.model.Movie;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import phoneticket.android.utils.ImageDownloader;
import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
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
	public static final String STATE_MOVIE_ID = "state.detailmovie.id";
	public static final String STATE_MOVIE_TITLE = "state.detailmovie.title";
	public static final String STATE_MOVIE_GENRE = "state.detailmovie.genre";
	public static final String STATE_MOVIE_DURATION = "state.detailmovie.duration";
	public static final String STATE_MOVIE_QUALIFICATION = "state.detailmovie.qualification";
	public static final String STATE_MOVIE_SYNOPSIS = "state.detailmovie.synopsis";
	public static final String STATE_MOVIE_IMAGEURL = "state.detailmovie.imageUrl";
	public static final String STATE_MOVIE_TRAILERURL = "state.detailmovie.trailerUrl";

	@Inject
	private IRetrieveMovieInfoService movieInfoService;

	@Inject
	private IRetrieveMovieFunctionsService movieFunctionsService;

	private boolean ignoreServicesCallbacks;

	private int movieId;
	private IMovie movie;

	private LinearLayout functionsLayout;

	private ArrayList<String> expandedGroupsIds;

	private IOnCinemaSelectedListener cinemaSelectedListener;

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

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;

		SharedPreferences preferences = getActivity().getPreferences(0);
		boolean recreatingState = movieId == preferences.getInt(STATE_MOVIE_ID,
				-1);
		Log.d("PhoneTicket", "onResume: "
				+ (recreatingState ? "recreating" : "creating") + " state");

		if (recreatingState) {
			movie = recreateMovieFromState();
			showMovie();
			hideProgressLayout();
		} else {
			retrieveMovieInfoAction();
		}
		retrieveMovieFunctionsAction();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (null != movie) {
			Log.d("PhoneTicket", "onPause: saving the state");
			SharedPreferences.Editor editor = getActivity().getPreferences(0)
					.edit();
			editor.putInt(STATE_MOVIE_ID, movie.getId());
			editor.putString(STATE_MOVIE_TITLE, movie.getTitle());
			editor.putString(STATE_MOVIE_GENRE, movie.getGenre());
			editor.putInt(STATE_MOVIE_DURATION, movie.getDurationInMinutes());
			editor.putString(STATE_MOVIE_QUALIFICATION,
					movie.getClasification());
			editor.putString(STATE_MOVIE_SYNOPSIS, movie.getSynopsis());
			editor.putString(STATE_MOVIE_IMAGEURL, movie.getImageURL());
			editor.putString(STATE_MOVIE_TRAILERURL, movie.getTrailerUrl());
			editor.commit();
		}
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d("PhoneTicket", "onDetach: clearing state");

		SharedPreferences.Editor editor = getActivity().getPreferences(0)
				.edit();
		editor.remove(STATE_MOVIE_ID);
		editor.remove(STATE_MOVIE_TITLE);
		editor.remove(STATE_MOVIE_GENRE);
		editor.remove(STATE_MOVIE_DURATION);
		editor.remove(STATE_MOVIE_QUALIFICATION);
		editor.remove(STATE_MOVIE_SYNOPSIS);
		editor.remove(STATE_MOVIE_IMAGEURL);
		editor.remove(STATE_MOVIE_TRAILERURL);
		editor.commit();
	}

	private void retrieveMovieFunctionsAction() {
		movieFunctionsService.retrieveMovieFunctions(this, movieId);
	}

	protected void retrieveMovieInfoAction() {
		movieInfoService.retrieveMovieInfo(this, movieId);
	}

	private IMovie recreateMovieFromState() {
		int durationInMinutes;
		String title, synopsis, imageURL, clasification, genre, youtubeVideoURL;

		SharedPreferences preferences = getActivity().getPreferences(0);
		title = preferences.getString(STATE_MOVIE_TITLE, "-");
		genre = preferences.getString(STATE_MOVIE_GENRE, "-");
		durationInMinutes = preferences.getInt(STATE_MOVIE_DURATION, 0);
		clasification = preferences.getString(STATE_MOVIE_QUALIFICATION, "-");
		synopsis = preferences.getString(STATE_MOVIE_SYNOPSIS, "-");
		imageURL = preferences.getString(STATE_MOVIE_IMAGEURL, "-");
		youtubeVideoURL = preferences.getString(STATE_MOVIE_TRAILERURL, "-");

		return new Movie(movieId, title, synopsis, imageURL, clasification,
				durationInMinutes, genre, youtubeVideoURL);
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
		if (ignoreServicesCallbacks) {
			return;
		}
		showMovie();
		hideProgressLayout();
	}

	@Override
	public void retrieveMovieInfoFinishWithError(
			IRetrieveMovieInfoService service, Integer errorCode) {
		if (ignoreServicesCallbacks) {
			return;
		}
		showProgressLayout(true);
	}

	@Override
	public void retrieveMovieFunctionsFinish(
			IRetrieveMovieFunctionsService service,
			final Collection<IMovieFunctions> moviesFunctions) {

		if (ignoreServicesCallbacks) {
			return;
		}
		functionsLayout.removeAllViews();
		int index = 0;
		for (final IMovieFunctions movieFunctions : moviesFunctions) {

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

			ImageButton goToCinemaContract = (ImageButton) headerView
					.findViewById(R.id.goToCinema);
			goToCinemaContract.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onGoToCinemaAction(movieFunctions.getCinemaId());
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
		if (ignoreServicesCallbacks) {
			return;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			cinemaSelectedListener = (IOnCinemaSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IOnCinemaSelectedListener");
		}
	}

	protected void onGoToCinemaAction(int cinemaId) {
		cinemaSelectedListener.onCinemaSelected(cinemaId);
	}

	public interface IOnCinemaSelectedListener {
		public void onCinemaSelected(int cinemaId);
	}
}
