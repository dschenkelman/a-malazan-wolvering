package phoneticket.android.activities.fragments;

import phoneticket.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class UserShowsFragment extends RoboFragment {

	@SuppressWarnings("unused")
	private boolean ignoreServicesCallbacks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_shows, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		/*try {
			myFunctionsListener = (IUserShowsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IMyFunctionsListener");
		}*/
	}

}
