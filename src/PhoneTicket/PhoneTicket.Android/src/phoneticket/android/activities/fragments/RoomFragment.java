package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import phoneticket.android.R;
import phoneticket.android.adapter.ArmChairAdapter;
import phoneticket.android.adapter.ImageAdapter;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;
import pl.polidea.view.ZoomView;
import roboguice.fragment.RoboFragment;

public class RoomFragment extends RoboFragment implements
		IRetrieveRoomInfoServiceDelegate {

	private static int MAX_ARMCHAIR = 6;
	public static final String TICKET_INFO = "ticket.info";

	@Inject
	private IRetrieveRoomInfoService roomInfoService;
	private boolean ignoreServicesCallbacks;
	private Ticket ticket;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_room, container,
				false);
		ZoomView zoomView = new ZoomView(getActivity());
		View gridView = inflater.inflate(R.layout.room_grid_view, container,
				false);
		zoomView.addView(gridView);
		LinearLayout layout = (LinearLayout) fragment.findViewById(R.id.room);
		layout.addView(zoomView);
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
		this.roomInfoService.retrieveRoomInfo(this, ticket.getRoomId());
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void retrieveRoomInfoFinish(IRetrieveRoomInfoService service,
			Collection<Collection<Integer>> armChairs) {
		if (!ignoreServicesCallbacks) {
			this.createArmChairs(armChairs);
		}
	}

	@Override
	public void retrieveRoomInfoFinishWithError(
			IRetrieveRoomInfoService service, Integer errorCode) {
		if (!ignoreServicesCallbacks) {

		}
	}

	private void createArmChairs(Collection<Collection<Integer>> armChairs) {
		GridView armChairSelection = (GridView) getView().findViewById(
				R.id.armChairSelection);

		List<ArmChair> armChairsData = new ArrayList<ArmChair>();
		final ArmChairAdapter imageAdapter = new ArmChairAdapter(getActivity(),
				R.id.armChairView, armChairsData);
		for (Collection<Integer> row : armChairs) {
			int i = 0;
			for (Integer state : row) {
				int j = 0;
				armChairsData.add(new ArmChair(state, j, i));
				j++;
			}
			i++;
		}
		armChairSelection.setAdapter(imageAdapter);
		armChairSelection.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

			}
		});
		imageAdapter.notifyDataSetChanged();
	}
}
