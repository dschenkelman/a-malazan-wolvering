package phoneticket.android.activities.fragments;

import java.util.List;

import phoneticket.android.R;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.Ticket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import roboguice.fragment.RoboFragment;

public class PromotionFragment extends RoboFragment {

	public static String ARM_CHAIRS_SELECTED = "armchairs.selected";
	private List<ArmChair> armChairsSelected;
	private boolean ignoreServicesCallbacks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_promotion,
				container, false);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		armChairsSelected = (List<ArmChair>) getArguments().getSerializable(
				ARM_CHAIRS_SELECTED);
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
}
