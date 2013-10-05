package phoneticket.android.activities.fragments;

import phoneticket.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class DetailCinemaFragment extends RoboFragment {

	public static final String EXTRA_CINEMA_ID = "bundle.detailcinema.id";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_detail_cinema, container,
				false);
	}
}
