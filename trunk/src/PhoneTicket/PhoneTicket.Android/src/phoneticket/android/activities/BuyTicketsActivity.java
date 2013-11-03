package phoneticket.android.activities;

import java.util.List;

import com.google.inject.Inject;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.RegExpValidator;

import phoneticket.android.R;
import phoneticket.android.activities.dialog.ConfirmBackActionDialogFragment;
import phoneticket.android.activities.dialog.ConfirmBackActionDialogFragment.IConfirmBackActionDialogDelegate;
import phoneticket.android.model.CreditCard;
import phoneticket.android.services.get.IRetrieveCreditCardsServise;
import phoneticket.android.services.get.IRetrieveCreditCardsServiseDelegate;
import phoneticket.android.utils.UserManager;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

public class BuyTicketsActivity extends RoboFragmentActivity implements
		IRetrieveCreditCardsServiseDelegate, IConfirmBackActionDialogDelegate {

	public static final String EXTRA_RESULT_PURCHASE_CARD_NUMBER = "extra.buyticket.cardnumber";
	public static final String EXTRA_RESULT_PURCHASE_SECURIRY_NUMBER = "extra.buyticket.securittnumber";
	public static final String EXTRA_RESULT_PURCHASE_EXPIRATION = "extra.buyticket.vencimiento";
	public static final String EXTRA_RESULT_PURCHASE_COMPANY_ID = "extra.buyticket.cardtype";

	@Inject
	private IFormValidator purchaseForm;
	@Inject
	private IRetrieveCreditCardsServise service;

	private boolean ignoreServices;
	private CreditCard selectedCreditCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_tickets);

		createPurchaseForm();

		EditText firstName = (EditText) findViewById(R.id.firstNameEditText);
		EditText lastName = (EditText) findViewById(R.id.lastNameEditText);

		firstName.setText(UserManager.getInstance().getLogedUser()
				.getFirstName());
		lastName.setText(UserManager.getInstance().getLogedUser().getLastName());

		((Button) findViewById(R.id.buyButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBuyAction();
					}
				});

		((Button) findViewById(R.id.reloadDataButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onRetrieveCreditCardsAction();
					}
				});
		onRetrieveCreditCardsAction();
	}

	protected void onRetrieveCreditCardsAction() {
		showLoadingLayout();
		service.retrieveCreditCards(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		ignoreServices = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServices = true;
	}
	
	@Override
	public void onBackPressed() {
		ConfirmBackActionDialogFragment confirmDialog = new ConfirmBackActionDialogFragment();
		confirmDialog.show(getSupportFragmentManager(), "dialog.confirm");
	}

	private void createPurchaseForm() {
		EditText firstName = (EditText) findViewById(R.id.firstNameEditText);
		EditText lastName = (EditText) findViewById(R.id.lastNameEditText);
		EditText cardNumber = (EditText) findViewById(R.id.cardNumberEditText);
		EditText securityNumber = (EditText) findViewById(R.id.securityCodeEditText);
		EditText expiration = (EditText) findViewById(R.id.vencimientoEditText);

		Validate firstNameValidate = new Validate(firstName);
		Validate lastNameValidate = new Validate(lastName);
		Validate cardNumberValidate = new Validate(cardNumber);
		Validate securityNumberValidate = new Validate(securityNumber);
		Validate vencimientoValidate = new Validate(expiration);

		Context context = getApplicationContext();

		firstNameValidate.addValidator(new NotEmptyValidator(context));
		lastNameValidate.addValidator(new NotEmptyValidator(context));
		cardNumberValidate.addValidator(new NotEmptyValidator(context));
		securityNumberValidate.addValidator(new NotEmptyValidator(context));
		vencimientoValidate.addValidator(new NotEmptyValidator(context));

		RegExpValidator creditCardValidator = new RegExpValidator(context,
				R.string.validator_invalid_creditcardnumber);
		creditCardValidator.setPattern("([0-9]{4}-){3}[0-9]{4}");
		cardNumberValidate.addValidator(creditCardValidator);

		RegExpValidator securityNumberValidator = new RegExpValidator(context,
				R.string.validator_invalid_securitynumber);
		securityNumberValidator.setPattern("[0-9]{3}");
		securityNumberValidate.addValidator(securityNumberValidator);

		RegExpValidator vencimientoValidator = new RegExpValidator(context,
				R.string.validator_invalid_vencimiento);
		String longMonths = "((0?[1-9])|([1-2]?[0-9])|(3[0-1]))/(([0]?[13578])|(1[02]))/[0-9]{4}";
		String shortMonths = "((0?[1-9])|([1-2]?[0-9])|(30))/(([0]?[469])|(11))/[0-9]{4}";
		String feb = "((0?[1-9])|([1-2]?[0-9]))/(([0]?[2])|(11))/[0-9]{4}";
		vencimientoValidator.setPattern("(" + longMonths + ")|(" + shortMonths
				+ ")|(" + feb + ")");
		vencimientoValidate.addValidator(vencimientoValidator);

		purchaseForm.addValidates(firstNameValidate);
		purchaseForm.addValidates(lastNameValidate);
		purchaseForm.addValidates(cardNumberValidate);
		purchaseForm.addValidates(securityNumberValidate);
		purchaseForm.addValidates(vencimientoValidate);
	}

	private void createCreditCardPicker(List<CreditCard> creditCards) {
		final Spinner spinner = (Spinner) findViewById(R.id.cardCompanySppiner);
		ArrayAdapter<CreditCard> adapter = new ArrayAdapter<CreditCard>(this,
				android.R.layout.simple_spinner_dropdown_item, creditCards);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				CreditCard creditCard = (CreditCard) spinner.getSelectedItem();
				setSelectedCreditCard(creditCard);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		selectedCreditCard = creditCards.get(0);
	}

	protected void setSelectedCreditCard(CreditCard creditCard) {
		selectedCreditCard = creditCard;
	}

	private void showErrorLayout() {
		layoutVisibility(LinearLayout.VISIBLE, RelativeLayout.GONE,
				ScrollView.GONE);
	}

	private void showLoadingLayout() {
		layoutVisibility(LinearLayout.GONE, RelativeLayout.VISIBLE,
				ScrollView.GONE);
	}

	private void showFormLayout() {
		layoutVisibility(LinearLayout.GONE, RelativeLayout.GONE,
				ScrollView.VISIBLE);
	}

	private void layoutVisibility(int erroVisibility, int loadingVisibility,
			int myShowsVisibilty) {
		LinearLayout errorView = (LinearLayout) findViewById(R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) findViewById(R.id.loadingDataLayout);
		ScrollView myShows = (ScrollView) findViewById(R.id.cardDataScrollView);

		errorView.setVisibility(erroVisibility);
		loadingView.setVisibility(loadingVisibility);
		myShows.setVisibility(myShowsVisibilty);
	}

	protected void onBuyAction() {
		if (purchaseForm.validate()) {
			EditText cardNumber = (EditText) findViewById(R.id.cardNumberEditText);
			EditText securityNumber = (EditText) findViewById(R.id.securityCodeEditText);
			EditText vencimiento = (EditText) findViewById(R.id.vencimientoEditText);

			Intent resultData = new Intent();
			resultData.putExtra(EXTRA_RESULT_PURCHASE_CARD_NUMBER, cardNumber
					.getText().toString());
			resultData.putExtra(EXTRA_RESULT_PURCHASE_SECURIRY_NUMBER,
					securityNumber.getText().toString());
			resultData.putExtra(EXTRA_RESULT_PURCHASE_EXPIRATION, vencimiento
					.getText().toString());
			resultData.putExtra(EXTRA_RESULT_PURCHASE_COMPANY_ID,
					selectedCreditCard.getId());
			setResult(MasterActivity.PURCHASE_DATA_RESULT_CODE_OK, resultData);
			finish();
		}
	}

	@Override
	public void retrieveCreditCardsFinished(
			IRetrieveCreditCardsServise service, List<CreditCard> creditCards) {
		if (false == ignoreServices) {
			if (0 == creditCards.size()) {
				showErrorLayout();
			} else {
				createCreditCardPicker(creditCards);
				showFormLayout();
			}
		}
	}

	@Override
	public void retrieveCreditCardsFinishedWithError(
			IRetrieveCreditCardsServise service, int errorCode) {
		if (false == ignoreServices) {
			showErrorLayout();
		}
	}

	@Override
	public void onDialogPositiveClick() {
		finish();
	}
}