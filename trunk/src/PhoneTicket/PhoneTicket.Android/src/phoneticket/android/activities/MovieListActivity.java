package phoneticket.android.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import phoneticket.android.R;
import phoneticket.android.adapter.StaggeredAdapter;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MovieListActivity extends RoboActivity implements
		IRetrieveMovieListServiceDelegate {

	@Inject
	private IRetrieveMovieListService movieListService;

	@InjectView(R.id.staggeredGridView1)
	private StaggeredGridView gridView;

	private StaggeredAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);

		int margin = getResources().getDimensionPixelSize(R.dimen.image_margin);

		gridView.setItemMargin(margin);

		gridView.setPadding(margin, 0, margin, 0);

		movieListService.retrieveMovieList(this);
	}

	@Override
	public void retrieveMovieListFinish(IRetrieveMovieListService service,
			Collection<IMovieListItem> movieList) {
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
	}

	@Override
	public void retrieveMovieListFinishWithError(
			IRetrieveMovieListService service, Integer errorMessage) {

	}

}
