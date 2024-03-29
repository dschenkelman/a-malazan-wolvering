package phoneticket.android.activities.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IArmChairsSelected;
import phoneticket.android.activities.interfaces.IRibbonChangeMenuListener;
import phoneticket.android.adapter.ArmChairAdapter;
import phoneticket.android.adapter.ArmChairAdapter.ArmChairHolder;
import phoneticket.android.model.ArmChair;
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
	private IArmChairsSelected armChairsSelected;
	private boolean ignoreServicesCallbacks;
	private Ticket ticket;
	private List<ArmChair> selectedArmChair;
	private TextView armChairsCount;
	private int numColumns;
	private int firstColumn;
	private int lastColumn;
	private int firstRow;
	private int lastRow;
	private IRibbonChangeMenuListener ribbonListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_room, container,
				false);
		selectedArmChair = new ArrayList<ArmChair>();
		Button continueButton = (Button) fragment
				.findViewById(R.id.selectionFinished);
		continueButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedArmChair.size() > 0) {
					armChairsSelected.onArmChairsSelected(selectedArmChair,
							ticket);
				} else {
					Toast t = Toast.makeText(getActivity(),
							"Se debe seleccionar al menos una butaca",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
		armChairsCount = (TextView) fragment
				.findViewById(R.id.armChairSelected);
		ZoomView zoomView = new ZoomView(getActivity());
		View gridView = inflater.inflate(R.layout.room_grid_view, container,
				false);
		zoomView.addView(gridView);
		LinearLayout layout = (LinearLayout) fragment.findViewById(R.id.room);
		RelativeLayout loading = (RelativeLayout) inflater.inflate(
				R.layout.loading, container, false);
		RelativeLayout error = (RelativeLayout) inflater.inflate(
				R.layout.error, container, false);
		Button button = (Button) error.findViewById(R.id.refreshViewButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retreiveRoomInfo();
			}

		});
		layout.addView(loading);
		layout.addView(error);
		layout.addView(zoomView);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ticket = (Ticket) getArguments().getSerializable(TICKET_INFO);

		TextView cinemaAddress = (TextView) getView().findViewById(
				R.id.cinemaAddress);
		cinemaAddress.setText(ticket.getCinemaAddress());

		TextView movieDay = (TextView) getView().findViewById(R.id.movieDay);
		movieDay.setText(ticket.getFunctionDay() + "  "
				+ ticket.getFunctionTime());

		TextView movieName = (TextView) getView().findViewById(R.id.movieName);
		movieName.setText(ticket.getMovieTitle());

	}

	@Override
	public void onResume() {
		super.onResume();
		ribbonListener.setOnMoviesMenu();
		ignoreServicesCallbacks = false;
		retreiveRoomInfo();
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			armChairsSelected = (IArmChairsSelected) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IShareButtonsVisibilityListener");
		}
		try {
			ribbonListener = (IRibbonChangeMenuListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IRibbonChangeMenuListener");
		}
	}

	private void retreiveRoomInfo() {
		RelativeLayout error = (RelativeLayout) getView().findViewById(
				R.id.errorViewContainer);
		error.setVisibility(RelativeLayout.GONE);
		RelativeLayout loading = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		loading.setVisibility(RelativeLayout.VISIBLE);
		GridView armChairSelection = (GridView) getView().findViewById(
				R.id.armChairSelection);
		armChairSelection.setVisibility(GridView.GONE);
		this.roomInfoService.retrieveRoomInfo(this, ticket.getFunctionId());
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
			RelativeLayout error = (RelativeLayout) getView().findViewById(
					R.id.errorViewContainer);
			error.setVisibility(RelativeLayout.VISIBLE);
			RelativeLayout loading = (RelativeLayout) getView().findViewById(
					R.id.loadingDataLayout);
			loading.setVisibility(RelativeLayout.GONE);
			GridView armChairSelection = (GridView) getView().findViewById(
					R.id.armChairSelection);
			armChairSelection.setVisibility(GridView.GONE);
		}
	}

	private void createArmChairs(Collection<Collection<Integer>> armChairs) {
		RelativeLayout error = (RelativeLayout) getView().findViewById(
				R.id.errorViewContainer);
		error.setVisibility(RelativeLayout.GONE);
		RelativeLayout loading = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		loading.setVisibility(RelativeLayout.GONE);
		GridView armChairSelection = (GridView) getView().findViewById(
				R.id.armChairSelection);
		armChairSelection.setVisibility(GridView.VISIBLE);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		this.checkColumnsToShow(armChairs);
		armChairSelection.setNumColumns(numColumns);
		List<ArmChair> armChairsData = new ArrayList<ArmChair>();
		Resources resources = getResources();
		String[] letters = resources.getStringArray(R.array.letters);
		int rowNumber = 0;
		for (Collection<Integer> row : armChairs) {
			if (rowNumber >= this.firstRow && rowNumber <= this.lastRow) {
				int columnNumber = 0;
				for (Integer state : row) {
					if (columnNumber >= firstColumn
							&& columnNumber <= lastColumn) {
						armChairsData.add(new ArmChair(state, columnNumber + 1,
								letters[rowNumber]));
					}
					columnNumber++;
				}
			}
			rowNumber++;
		}

		final ArmChairAdapter imageAdapter = new ArmChairAdapter(getActivity(),
				R.id.armChairView, armChairsData, numColumns, width);
		armChairSelection.setAdapter(imageAdapter);
		armChairSelection.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				ArmChair armChair = imageAdapter.getItem(position);
				if (armChair.libre()) {
					if (selectedArmChair.size() < MAX_ARMCHAIR) {
						armChair.seleccionar();
						selectedArmChair.add(armChair);
					} else {
						Toast t = Toast.makeText(getActivity(),
								"El m�ximo de butacas posibles de seleccionar son: "
										+ MAX_ARMCHAIR, Toast.LENGTH_SHORT);
						t.show();
					}
				} else if (armChair.seleccionada()) {
					armChair.liberar();
					selectedArmChair.remove(armChair);
				}
				if (v != null) {
					ArmChairHolder holder = (ArmChairHolder) v.getTag();
					holder.getArmChairView().setState(armChair.getState());
					armChairsCount.setText(String.valueOf(selectedArmChair
							.size()));
				}
			}
		});
		armChairsCount.setText(String.valueOf(selectedArmChair.size()));
		imageAdapter.notifyDataSetChanged();
	}

	private void checkColumnsToShow(Collection<Collection<Integer>> armChairs) {
		this.numColumns = 0;
		this.firstColumn = 21;
		this.lastColumn = 0;
		this.firstRow = 16;
		this.lastRow = 0;
		int rowNumber = 0;
		for (Collection<Integer> rows : armChairs) {
			int columnNumber = 0;
			for (Integer columnState : rows) {
				if (columnState == ArmChair.LIBRE) {
					if (columnNumber < firstColumn) {
						firstColumn = columnNumber;
					}
					if (columnNumber > lastColumn) {
						lastColumn = columnNumber;
					}
					if (rowNumber < firstRow) {
						firstRow = rowNumber;
					}
					if (rowNumber > lastRow) {
						lastRow = rowNumber;
					}
				}
				columnNumber++;
			}
			rowNumber++;
		}
		numColumns = (lastColumn - firstColumn) > numColumns ? (lastColumn - firstColumn)
				: numColumns;
		numColumns++;

	}

}
