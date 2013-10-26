package phoneticket.android.activities.fragments;

import java.util.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import phoneticket.android.R;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;
import roboguice.fragment.RoboFragment;

public class RoomFragment extends RoboFragment implements
		IRetrieveRoomInfoServiceDelegate {

	private static int MAX_ARMCHAIR = 6;
	public static final String TICKET_INFO = "ticket.info";

	// @Inject
	// private IRetrieveRoomInfoService roomInfoService;
	private boolean ignoreServicesCallbacks;
	private Ticket ticket;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_room, container,
				false);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ticket = (Ticket) getArguments().getSerializable(TICKET_INFO);
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;
		// this.roomInfoService.retrieveRoomInfo(this, roomId);
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void retrieveRoomInfoFinish(IRetrieveRoomInfoService service,
			Collection<Collection<Integer>> movieList) {
		if (!ignoreServicesCallbacks) {

		}
	}

	@Override
	public void retrieveRoomInfoFinishWithError(
			IRetrieveRoomInfoService service, Integer errorCode) {
		if (!ignoreServicesCallbacks) {

		}
	}

}
