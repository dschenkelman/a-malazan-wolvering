package phoneticket.android.activities.fragments;

import java.util.Collection;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.model.IMyShow;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class UserShowsFragment extends RoboFragment implements
		IRetrieveMyShowsServiceDelegate {

	private boolean ignoreServicesCallbacks;
	
	@Inject
	private IRetrieveMyShowsService myShowsService;

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
		
		myShowsService.retrieveMyShows(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void retrieveMyShowsServiceFinished(IRetrieveMyShowsService service,
			Collection<IMyShow> myShows) {
		if (false == ignoreServicesCallbacks) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void retrieveMyShowsServiceFinishedWithError(
			IRetrieveMyShowsService service, int errorCode) {
		if (false == ignoreServicesCallbacks) {
			// TODO Auto-generated method stub
		}
	}

}
