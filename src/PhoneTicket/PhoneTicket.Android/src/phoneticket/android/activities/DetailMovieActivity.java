package phoneticket.android.activities;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.ProgressDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.activities.dialog.ProgressDialogFragment.IProgressDialogDataSource;
import phoneticket.android.model.IMovie;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import phoneticket.android.utils.ImageDownloader;
import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

public class DetailMovieActivity extends RoboFragmentActivity implements
		IRetrieveMovieInfoServiceDelegate, IMessageDialogDataSource,
		IProgressDialogDataSource {

	public static final String MovieToShowId = "DetailMovieActivity.MovieToShowId";

	@Inject
	private IRetrieveMovieInfoService service;

	private IMovie movie;

	private String messageDialogBody;
	private String messageDialogTitle;

	private ProgressDialogFragment progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_movie);

		int movieId = getIntent().getIntExtra(
				DetailMovieActivity.MovieToShowId, -1);

		showProgressDialog();
		service.retrieveMovieInfo(this, movieId);
	}

	private void showMovie() {
		TextView title = (TextView) findViewById(R.id.movieTitleText);
		TextView gendre = (TextView) findViewById(R.id.movieGendreText);
		TextView duration = (TextView) findViewById(R.id.movieDurationText);
		TextView clasification = (TextView) findViewById(R.id.movieClasificationText);
		TextView synopsis = (TextView) findViewById(R.id.movieSynopsisText);

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

		ImageView poster = (ImageView) findViewById(R.id.movieImage);
		ImageDownloader downloader = new ImageDownloader(movie.getImageURL(),
				poster, null, null);
		downloader.execute();
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
		hideProgressDialog();
	}

	@Override
	public void retrieveMovieInfoFinishWithError(
			IRetrieveMovieInfoService service, Integer errorMessage) {
		hideProgressDialog();
		messageDialogBody = "No se pudo conectar con el servidor";
		messageDialogTitle = "Error";
		MessageDialogFragment dialog = new MessageDialogFragment();
		dialog.show(getSupportFragmentManager(), "dialog.error");
	}

	@Override
	public String getMessageTitle() {
		return messageDialogTitle;
	}

	private void showProgressDialog() {
		if (null == progressDialog)
			progressDialog = new ProgressDialogFragment();
		progressDialog.show(getSupportFragmentManager(), "dialog.progress");
	}

	private void hideProgressDialog() {
		progressDialog.dismiss();
	}

	@Override
	public String getProgressMessageTitle() {
		return "Buscando Lista de Películas";
	}

	@Override
	public String getMessage() {
		return messageDialogBody;
	}

}
