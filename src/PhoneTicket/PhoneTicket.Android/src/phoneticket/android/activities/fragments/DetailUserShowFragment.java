package phoneticket.android.activities.fragments;

import phoneticket.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class DetailUserShowFragment extends RoboFragment {
	
	public static final String USER_SHOW_INFO = "usershow.info";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_user_shows, container,
				false);
		return view;
	}
}
