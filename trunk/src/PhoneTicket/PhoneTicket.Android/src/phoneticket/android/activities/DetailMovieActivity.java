package phoneticket.android.activities;

import phoneticket.android.R;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class DetailMovieActivity extends Activity {

	public static final String MovieToShowId = "DetailMovieActivity.MovieToShowId";

	private IMovie movie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_movie);

		movie = createMockMovie();
		showMovie();
	}

	private IMovie createMockMovie() {
		return new Movie(
				1,
				"El Conjuro",
				"El conjuro se basa en los sucesos sobrenaturales que ocurrieron en la casa de Rhode Island de la familia Perron y que investigaron Ed y Lorraine Warren, expertos en actividades paranormales",
				"", "P16", 112, "Terror",
				"http://www.youtube.com/watch?v=OJgDCNyfWfQ");
	}

	private void showMovie() {
		TextView title = (TextView) findViewById(R.id.movieTitleText);
		TextView gendre = (TextView) findViewById(R.id.movieGendreText);
		TextView duration = (TextView) findViewById(R.id.movieDurationText);
		TextView clasification = (TextView) findViewById(R.id.movieClasificationText);
		TextView synopsis = (TextView) findViewById(R.id.movieSynopsisText);

		String gendreString = getResources().getString(
				R.string.detailMovieGendreStartText)
				+ movie.getGendre();
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
	}

	public void onWatchTrailerButtonAction(View sender) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie
				.getYoutubeVideoURL()));
		startActivity(browserIntent);
	}
}
