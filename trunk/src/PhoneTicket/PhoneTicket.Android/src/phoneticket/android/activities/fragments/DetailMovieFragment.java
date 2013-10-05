package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.model.IMovie;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;
import roboguice.fragment.RoboFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DetailMovieFragment extends RoboFragment implements
		IRetrieveMovieInfoServiceDelegate {
	
	public static final String EXTRA_MOVIE_ID = "bundle.detailmovie.id";
	
	@Inject
	private IRetrieveMovieInfoService service;

	private IMovie movie;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
		Button mockButton = (Button) view.findViewById(R.id.watchTrailerButton);
		mockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onWatchTrailerButtonAction(v);
			}
		});

		int movieId = getArguments().getInt(DetailMovieFragment.EXTRA_MOVIE_ID);
		showProgressDialog();
		service.retrieveMovieInfo(this, movieId);
		
		return view;
	}
	
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		
	}

	public void onWatchTrailerButtonAction(View sender) {
		
	}

	@Override
	public void retrieveMovieInfoFinish(IRetrieveMovieInfoService service,
			IMovie movie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveMovieInfoFinishWithError(
			IRetrieveMovieInfoService service, Integer errorCode) {
		// TODO Auto-generated method stub
		
	}
}
