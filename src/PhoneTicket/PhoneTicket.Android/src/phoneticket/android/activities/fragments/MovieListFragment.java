package phoneticket.android.activities.fragments;

import phoneticket.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MovieListFragment extends Fragment {
	
	private IOnMovieSelectedListener movieSelectedListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_movielist, container, false);
		Button mockButton = (Button) fragment.findViewById(R.id.mockMovieButton);
		mockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMockButtonClick(v);
			}
		});
		return fragment;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	movieSelectedListener = (IOnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMovieSelectedListener");
        }
    }
    
	public void onMockButtonClick(View sender) {
		movieSelectedListener.onSelectedSelected(1);
	}
	
    public interface IOnMovieSelectedListener {
        public void onSelectedSelected(int movieId);
    }
}
