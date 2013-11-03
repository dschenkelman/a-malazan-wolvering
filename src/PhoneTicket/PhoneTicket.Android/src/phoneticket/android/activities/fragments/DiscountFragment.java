package phoneticket.android.activities.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.BuyTicketsActivity;
import phoneticket.android.activities.MasterActivity.IOnPurchaseDataResultListener;
import phoneticket.android.activities.fragments.dialogs.ErrorDialogFragment;
import phoneticket.android.activities.fragments.dialogs.ProgressDialog;
import phoneticket.android.activities.fragments.dialogs.SuccessTicketSentDialogFragment;
import phoneticket.android.activities.interfaces.IDiscountSelectedListener;
import phoneticket.android.activities.interfaces.IToMovieListListener;
import phoneticket.android.adapter.DiscountAdapter;
import phoneticket.android.model.ArmChair;
import phoneticket.android.model.Discount;
import phoneticket.android.model.DiscountCountable;
import phoneticket.android.model.PostedArmChair;
import phoneticket.android.model.PostedDiscounts;
import phoneticket.android.model.PurchaseTicket;
import phoneticket.android.model.ReserveTicket;
import phoneticket.android.model.Ticket;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveDiscountsServiceDelegate;
import phoneticket.android.services.post.IRegisterDiscountsService;
import phoneticket.android.services.post.IRegisterDiscountsServiceDelegate;
import phoneticket.android.services.post.IRegisterPurchaseService;
import phoneticket.android.services.post.IRegisterPurchaseServiceDelegate;
import phoneticket.android.services.post.IRegisterReservationService;
import phoneticket.android.services.post.IRegisterReservationServiceDelegate;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import roboguice.fragment.RoboFragment;

public class DiscountFragment extends RoboFragment implements
		IRetrieveDiscountsServiceDelegate, IDiscountSelectedListener,
		IRegisterReservationServiceDelegate, IOnPurchaseDataResultListener,
		IRegisterPurchaseServiceDelegate,
		android.content.DialogInterface.OnClickListener,
		IRegisterDiscountsServiceDelegate {

	private static final int TWO_PAID_ONE = 0;
	private static final int PRICE = 1;
	private static final int PERCENTAGE = 2;
	private static final long ONE_HOUR = 3600 * 1000;
	public static String TICKET = "ticket";
	public static String ARM_CHAIRS_SELECTED = "armchairs.selected";

	@Inject
	private IRetrieveDiscountService retrieveDiscountService;
	@Inject
	private IRegisterReservationService reservationService;
	@Inject
	private IRegisterPurchaseService purchaseService;
	@Inject
	private IRegisterDiscountsService discountsService;

	private List<ArmChair> armChairsSelected;
	private boolean ignoreServicesCallbacks;
	private Ticket ticket;
	private int remainChairWithoutDiscount;
	private String cardNumber;
	private String securityNumber;
	private String vencimiento;
	private int cardType;
	private ArrayList<DiscountCountable> discountsBaseInfo;
	private String uuid;
	private IToMovieListListener toMovieListListener;
	private boolean ticketSent;
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.fragment_discount, container,
				false);
		LinearLayout layout = (LinearLayout) fragment
				.findViewById(R.id.discountsLayout);
		RelativeLayout loading = (RelativeLayout) inflater.inflate(
				R.layout.loading, container, false);
		RelativeLayout error = (RelativeLayout) inflater.inflate(
				R.layout.error, container, false);
		Button button = (Button) error.findViewById(R.id.refreshViewButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retrieveDiscountsInfo();
			}

		});

		((Button) fragment.findViewById(R.id.reservarButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onReservationButtonAction();
					}
				});
		((Button) fragment.findViewById(R.id.purchaseButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onPurchaseAction();
					}
				});
		((Button) fragment.findViewById(R.id.cancelarButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						goToMovieList();
					}
				});

		layout.addView(loading);
		layout.addView(error);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.toMovieListListener = (IToMovieListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IShareButtonsVisibilityListener");
		}
	}

	protected void onReservationButtonAction() {
		if (this.lessThanOneHourToShow()) {
			showProgressDialog();
			if (!this.ticketSent) {
				reservationService.reserveTicket(this, createReserveTicket());
			} else {
				onTicketSent();
			}
		} else {
			Toast t = Toast.makeText(getActivity(),
					"Falta menos de una hora para el comienzo de la función",
					Toast.LENGTH_SHORT);
			t.show();
		}
	}

	private ReserveTicket createReserveTicket() {
		ReserveTicket ticket = new ReserveTicket(this.ticket.getFunctionId(),
				this.createPostArmChairs());
		return ticket;
	}

	protected void onPurchaseAction() {
		if (this.cardNumber == null || this.securityNumber == null
				|| this.vencimiento == null) {
			Intent intent = new Intent(getActivity(), BuyTicketsActivity.class);
			startActivityForResult(intent, 31);
		} else {
			showProgressDialog();
			if (!this.ticketSent) {
				this.purchaseService.purchaseTicket(this,
						createPurchaseTicket());
			} else {
				onTicketSent();
			}
		}
	}

	private PurchaseTicket createPurchaseTicket() {
		PurchaseTicket purchaseTicket = new PurchaseTicket(
				this.ticket.getFunctionId(), this.createPostArmChairs(),
				this.cardNumber, this.securityNumber, this.vencimiento,
				this.cardType);
		return purchaseTicket;
	}

	private List<PostedArmChair> createPostArmChairs() {
		List<PostedArmChair> postedArmChairs = new ArrayList<PostedArmChair>();
		for (ArmChair armChair : armChairsSelected) {
			int rowInt = armChair.getRow().toCharArray()[0] - 'A' + 1;
			PostedArmChair posted = new PostedArmChair(rowInt,
					armChair.getColumn());
			postedArmChairs.add(posted);
		}
		return postedArmChairs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		armChairsSelected = (List<ArmChair>) getArguments().getSerializable(
				ARM_CHAIRS_SELECTED);
		remainChairWithoutDiscount = armChairsSelected.size();
		ticket = (Ticket) getArguments().getSerializable(TICKET);
		((TextView) getView().findViewById(R.id.filmInfo)).setText(ticket
				.getMovieTitle()
				+ " - "
				+ ticket.getFunctionDay()
				+ "  "
				+ ticket.getFunctionTime());
		((TextView) getView().findViewById(R.id.cinemaAddress)).setText(ticket
				.getCinemaAddress());
		String armChairs = "";
		for (ArmChair armChair : armChairsSelected) {
			armChairs += armChair.getRow() + armChair.getColumn() + " ";
		}
		((TextView) getView().findViewById(R.id.cinemaAddress)).setText(ticket
				.getCinemaAddress());
		((TextView) getView().findViewById(R.id.armChairs))
				.setText(armChairsSelected.size() + " Butacas: " + armChairs);

		((TextView) getView().findViewById(R.id.amount)).setText(String
				.valueOf(armChairsSelected.size() * ticket.getPrice()));
		((TextView) getView().findViewById(R.id.total)).setText(String
				.valueOf(armChairsSelected.size() * ticket.getPrice()));
		((Button) getView().findViewById(R.id.reservarButton)).setEnabled(this
				.lessThanOneHourToShow());
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServicesCallbacks = false;
		if (this.discountsBaseInfo != null) {
			this.fieldDiscountsList(null);
		} else {
			retrieveDiscountsInfo();
		}
	}

	private void retrieveDiscountsInfo() {
		RelativeLayout error = (RelativeLayout) getView().findViewById(
				R.id.errorViewContainer);
		error.setVisibility(RelativeLayout.GONE);
		RelativeLayout loading = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		loading.setVisibility(RelativeLayout.VISIBLE);
		ListView discounts = (ListView) getView().findViewById(R.id.discounts);
		discounts.setVisibility(ListView.GONE);
		this.retrieveDiscountService.retrieveDiscounts(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void retrieveDiscountsFinish(
			IRetrieveDiscountService retrieveDiscountsService,
			Collection<Discount> discounts) {
		if (!ignoreServicesCallbacks) {
			fieldDiscountsList(discounts);
		}
	}

	private void fieldDiscountsList(Collection<Discount> discounts) {
		RelativeLayout error = (RelativeLayout) getView().findViewById(
				R.id.errorViewContainer);
		error.setVisibility(RelativeLayout.GONE);
		RelativeLayout loading = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		loading.setVisibility(RelativeLayout.GONE);
		ListView discountsListView = (ListView) getView().findViewById(
				R.id.discounts);
		discountsListView.setVisibility(ListView.VISIBLE);
		if (this.discountsBaseInfo == null) {
			discountsBaseInfo = new ArrayList<DiscountCountable>();
			for (Discount discount : discounts) {
				DiscountCountable discountCountable = new DiscountCountable(
						discount);
				discountsBaseInfo.add(discountCountable);
			}
		}
		DiscountAdapter discountAdapter = new DiscountAdapter(getActivity(),
				R.id.discountRow, discountsBaseInfo);
		discountAdapter.setDiscountSelectedListener(this);
		discountsListView.setAdapter(discountAdapter);
		discountAdapter.notifyDataSetChanged();
	}

	@Override
	public void retrieveDiscountsFinishWithError(
			IRetrieveDiscountService retrieveDiscountsService, int statusCode) {
		if (!ignoreServicesCallbacks) {
			RelativeLayout error = (RelativeLayout) getView().findViewById(
					R.id.errorViewContainer);
			error.setVisibility(RelativeLayout.VISIBLE);
			RelativeLayout loading = (RelativeLayout) getView().findViewById(
					R.id.loadingDataLayout);
			loading.setVisibility(RelativeLayout.GONE);
			ListView armChairSelection = (ListView) getView().findViewById(
					R.id.discounts);
			armChairSelection.setVisibility(ListView.GONE);
		}
	}

	@Override
	public boolean discountIncrease(DiscountCountable discount) {
		TextView totalText = (TextView) getView().findViewById(R.id.total);
		Double total = Double.valueOf(String.valueOf(totalText.getText()));
		switch (discount.getType()) {
		case TWO_PAID_ONE: {
			if (remainChairWithoutDiscount >= 2) {
				remainChairWithoutDiscount -= 2;
				total -= ticket.getPrice();
				totalText.setText(total.toString());
				return true;
			}
			break;
		}
		case PRICE: {
			if (remainChairWithoutDiscount >= 1) {
				remainChairWithoutDiscount -= 1;
				total -= discount.getValue();
				totalText.setText(total.toString());
				return true;
			}
			break;
		}
		case PERCENTAGE: {
			if (remainChairWithoutDiscount >= 1) {
				remainChairWithoutDiscount -= 1;
				total -= ticket.getPrice() * discount.getValue();
				totalText.setText(total.toString());
				return true;
			}
			break;
		}
		}
		return false;
	}

	@Override
	public void discountDecrease(DiscountCountable discount) {
		TextView totalText = (TextView) getView().findViewById(R.id.total);
		Double total = Double.valueOf(String.valueOf(totalText.getText()));
		switch (discount.getType()) {
		case TWO_PAID_ONE: {
			remainChairWithoutDiscount += 2;
			total += ticket.getPrice();
			totalText.setText(total.toString());
			break;
		}
		case PRICE: {
			remainChairWithoutDiscount += 1;
			total += discount.getValue();
			totalText.setText(total.toString());
			break;
		}
		case PERCENTAGE: {
			remainChairWithoutDiscount += 1;
			total += ticket.getPrice() * discount.getValue();
			totalText.setText(total.toString());
			break;
		}
		}
	}

	@Override
	public boolean discountPosible(DiscountCountable discount) {
		switch (discount.getType()) {
		case TWO_PAID_ONE: {
			return remainChairWithoutDiscount >= 2;
		}
		case PRICE: {
			return remainChairWithoutDiscount >= 1;
		}
		case PERCENTAGE: {
			return remainChairWithoutDiscount >= 1;
		}
		}
		return false;
	}

	@Override
	public void discountUnSelected(DiscountCountable discount) {
		TextView totalText = (TextView) getView().findViewById(R.id.total);
		Double total = Double.valueOf(String.valueOf(totalText.getText()));
		switch (discount.getType()) {
		case TWO_PAID_ONE: {
			remainChairWithoutDiscount += 2 * discount.getCount();
			total += ticket.getPrice() * discount.getCount();
			totalText.setText(total.toString());
			break;
		}
		case PRICE: {
			remainChairWithoutDiscount += 1 * discount.getCount();
			total += discount.getValue() * discount.getCount();
			totalText.setText(total.toString());
			break;
		}
		case PERCENTAGE: {
			remainChairWithoutDiscount += 1 * discount.getCount();
			total += ticket.getPrice() * discount.getValue()
					* discount.getCount();
			totalText.setText(total.toString());
			break;
		}
		}
	}

	@Override
	public void onPurchaseDataResult(String cardNumber, String securityNumber,
			String expiration, int companyId) {
		this.cardNumber = cardNumber;
		this.securityNumber = securityNumber;
		this.vencimiento = expiration;
		this.cardType = companyId;
		onPurchaseAction();
	}

	@Override
	public void purchaseTicketFinish(IRegisterPurchaseService service,
			String uuid) {
		this.uuid = uuid.replaceAll("\"", "");
		this.ticketSent = true;
		this.onTicketSent();
	}

	@Override
	public void reserveTicketFinish(IRegisterReservationService service,
			String uuid) {
		this.uuid = uuid.replaceAll("\"", "");
		this.ticketSent = true;
		this.onTicketSent();
	}

	private void onTicketSent() {
		List<PostedDiscounts> discounts = this.getDiscountsSelected();
		if (discounts.size() > 0) {
			this.discountsService.registerDiscounts(this, discounts, this.uuid);
		} else {
			this.createSuccessTicketSentDialog();
		}
	}

	@Override
	public void registerDiscountsFinish(IRegisterDiscountsService service) {
		this.createSuccessTicketSentDialog();
	}

	@Override
	public void purchaseTicketFinishWithError(IRegisterPurchaseService service,
			int statusCode) {
		this.ticketSent = false;
		hideProgressDialog();
		ErrorDialogFragment dialog = new ErrorDialogFragment();
		dialog.setMessage("Error comprando el ticket. Intente Nuevamente");
		dialog.show(getFragmentManager(), "dialog.error.ticketpurchase");
	}

	@Override
	public void reserveTicketFinishWithError(
			IRegisterReservationService service, int statusCode) {
		this.ticketSent = false;
		hideProgressDialog();
		ErrorDialogFragment dialog = new ErrorDialogFragment();
		dialog.setMessage("Error reservando el ticket. Intente Nuevamente");
		dialog.show(getFragmentManager(), "dialog.error.ticketreservation");
	}

	@Override
	public void registerDiscountsFinishWithError(
			IRegisterDiscountsService service, int statusCode) {
		hideProgressDialog();
		ErrorDialogFragment dialog = new ErrorDialogFragment();
		dialog.setMessage("Error registrando las promociones. Intente Nuevamente");
		dialog.show(getFragmentManager(), "dialog.error.ticketreservation");
	}

	private void createSuccessTicketSentDialog() {
		hideProgressDialog();
		SuccessTicketSentDialogFragment dialog = new SuccessTicketSentDialogFragment();
		dialog.setListener(this);
		dialog.show(getFragmentManager(), "dialog.success.ticketsent");
	}

	private List<PostedDiscounts> getDiscountsSelected() {
		List<PostedDiscounts> discountsSelected = new ArrayList<PostedDiscounts>();
		for (DiscountCountable discount : this.discountsBaseInfo) {
			if (discount.isSelected())
				discountsSelected.add(new PostedDiscounts(discount.getId(),
						discount.getCount()));
		}
		return discountsSelected;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		goToMovieList();
	}

	private void goToMovieList() {
		toMovieListListener.toMovieList();
	}

	private void showProgressDialog() {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog();
			this.progressDialog.show(getFragmentManager(), "progress.dialog");
		}
	}

	private void hideProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	private boolean lessThanOneHourToShow() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
					Locale.ENGLISH);
			Date show = df.parse(this.ticket.getFunctionDay() + " "
					+ this.ticket.getFunctionTime() + ":00");
			Date now = new Date();
			return (show.getTime() - now.getTime()) > (ONE_HOUR);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
