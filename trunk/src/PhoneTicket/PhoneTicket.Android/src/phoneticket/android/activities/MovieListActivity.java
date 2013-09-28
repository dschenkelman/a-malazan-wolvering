package phoneticket.android.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import phoneticket.android.R;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.activities.dialog.ProgressDialogFragment;
import phoneticket.android.activities.dialog.MessageDialogFragment.IMessageDialogDataSource;
import phoneticket.android.activities.dialog.ProgressDialogFragment.IProgressDialogDataSource;
import phoneticket.android.adapter.StaggeredAdapter;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.inject.Inject;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class MovieListActivity extends RoboFragmentActivity implements
		IRetrieveMovieListServiceDelegate, IMessageDialogDataSource,
		IProgressDialogDataSource {

	@Inject
	private IRetrieveMovieListService movieListService;

	@InjectView(R.id.movieListContainer)
	private StaggeredGridView gridView;

	private StaggeredAdapter adapter;

	private String messageDialogBody;
	private String messageDialogTitle;

	private ProgressDialogFragment progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		int margin = getResources().getDimensionPixelSize(R.dimen.image_margin);

		gridView.setItemMargin(margin);

		gridView.setPadding(margin, 0, margin, 0);

		showProgressDialog();
		movieListService.retrieveMovieList(this);
	}

	public void onRefreshMovieListAction(View sender) {
		movieListService.retrieveMovieList(this);

		LinearLayout errorContainer = (LinearLayout) findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(LinearLayout.GONE);
	}

	@Override
	public void retrieveMovieListFinish(IRetrieveMovieListService service,
			Collection<IMovieListItem> movieList) {

		LinearLayout errorContainer = (LinearLayout) findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(LinearLayout.GONE);
		gridView.setVisibility(StaggeredGridView.VISIBLE);

		List<IMovieListItem> movies = new ArrayList<IMovieListItem>();
		movies.addAll(movieList);
		adapter = new StaggeredAdapter(MovieListActivity.this,
				R.id.scaleImageView, movies);

		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(StaggeredGridView parent, View view,
					int position, long id) {
				IMovieListItem movie = adapter.getItem(position);
				Intent intent = new Intent(MovieListActivity.this,
						DetailMovieActivity.class);
				intent.putExtra(DetailMovieActivity.MovieToShowId,
						movie.getId());
				startActivity(intent);
			}
		};

		gridView.setOnItemClickListener(listener);
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		hideProgressDialog();
	}

	@Override
	public void retrieveMovieListFinishWithError(
			IRetrieveMovieListService service, Integer errorCode) {
		hideProgressDialog();
		LinearLayout errorContainer = (LinearLayout) findViewById(R.id.errorViewContainer);
		errorContainer.setVisibility(LinearLayout.VISIBLE);
		gridView.setVisibility(StaggeredGridView.GONE);

		messageDialogBody = "No se pudo conectar con el servidor";
		messageDialogTitle = "Error";
		MessageDialogFragment dialog = new MessageDialogFragment();
		dialog.show(getSupportFragmentManager(), "dialog.error");
	}

	@Override
	public String getMessage() {
		return messageDialogBody;
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

}
