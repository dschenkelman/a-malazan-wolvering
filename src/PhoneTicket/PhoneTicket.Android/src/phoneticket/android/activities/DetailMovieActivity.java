package phoneticket.android.activities;

import phoneticket.android.R;
import android.os.Bundle;
import android.app.Activity;

public class DetailMovieActivity extends Activity {

	public static final String MovieToShowId = "DetailMovieActivity.MovieToShowId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_movie);
	}

}
