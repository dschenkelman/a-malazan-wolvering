package phoneticket.android.activities;

import com.google.inject.Inject;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.RegExpValidator;

import phoneticket.android.R;
import phoneticket.android.utils.UserManager;
import phoneticket.android.validator.IFormValidator;
import roboguice.activity.RoboFragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BuyTicketsActivity extends RoboFragmentActivity {

	public static final String EXTRA_RESULT_PURCHASE_CARD_NUMBER = "extra.buyticket.cardnumber";
	public static final String EXTRA_RESULT_PURCHASE_SECURIRY_NUMBER = "extra.buyticket.securittnumber";
	public static final String EXTRA_RESULT_PURCHASE_EXPIRATION = "extra.buyticket.vencimiento";
	public static final String EXTRA_RESULT_PURCHASE_COMPANY_ID = "extra.buyticket.cardtype";

	@Inject
	private IFormValidator purchaseForm;

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
	}

	private void createPurchaseForm() {
		EditText firstName = (EditText) findViewById(R.id.firstNameEditText);
		EditText lastName = (EditText) findViewById(R.id.lastNameEditText);
		EditText cardNumber = (EditText) findViewById(R.id.cardNumberEditText);
		EditText securityNumber = (EditText) findViewById(R.id.securityCodeEditText);
		EditText vencimiento = (EditText) findViewById(R.id.vencimientoEditText);
		EditText cardType = (EditText) findViewById(R.id.cardTypeEditText);

		Validate firstNameValidate = new Validate(firstName);
		Validate lastNameValidate = new Validate(lastName);
		Validate cardNumberValidate = new Validate(cardNumber);
		Validate securityNumberValidate = new Validate(securityNumber);
		Validate vencimientoValidate = new Validate(vencimiento);
		Validate cardTypeValidate = new Validate(cardType);

		Context context = getApplicationContext();

		firstNameValidate.addValidator(new NotEmptyValidator(context));
		lastNameValidate.addValidator(new NotEmptyValidator(context));
		cardNumberValidate.addValidator(new NotEmptyValidator(context));
		securityNumberValidate.addValidator(new NotEmptyValidator(context));
		vencimientoValidate.addValidator(new NotEmptyValidator(context));
		cardTypeValidate.addValidator(new NotEmptyValidator(context));

		RegExpValidator creditCardValidator = new RegExpValidator(context, R.string.validator_invalid_creditcardnumber);
		creditCardValidator.setPattern("[0-9]{16}");
		cardNumberValidate.addValidator(creditCardValidator);

		RegExpValidator securityNumberValidator = new RegExpValidator(context, R.string.validator_invalid_securitynumber);
		securityNumberValidator.setPattern("[0-9]{3}");
		securityNumberValidate.addValidator(securityNumberValidator);

		RegExpValidator vencimientoValidator = new RegExpValidator(context, R.string.validator_invalid_vencimiento);
		String longMonths = "(([0]?[13578])|(1[02]))/((0?[1-9])|([1-2]?[0-9])|(3[0-1]))";
		String shortMonths = "(([0]?[469])|(11))/((0?[1-9])|([1-2]?[0-9])|(30))";
		String feb = "(([0]?[2])|(11))/((0?[1-9])|([1-2]?[0-9]))";
		vencimientoValidator.setPattern("(" + longMonths + ")|(" + shortMonths + ")|(" + feb + ")");
		vencimientoValidate.addValidator(vencimientoValidator);
		
		purchaseForm.addValidates(firstNameValidate);
		purchaseForm.addValidates(lastNameValidate);
		purchaseForm.addValidates(cardNumberValidate);
		purchaseForm.addValidates(securityNumberValidate);
		purchaseForm.addValidates(vencimientoValidate);
		purchaseForm.addValidates(cardTypeValidate);
	}

	protected void onBuyAction() {
		if (purchaseForm.validate()) {
			EditText cardNumber = (EditText) findViewById(R.id.cardNumberEditText);
			EditText securityNumber = (EditText) findViewById(R.id.securityCodeEditText);
			EditText vencimiento = (EditText) findViewById(R.id.vencimientoEditText);
			EditText cardType = (EditText) findViewById(R.id.cardTypeEditText);

			Intent resultData = new Intent();
			resultData.putExtra(EXTRA_RESULT_PURCHASE_CARD_NUMBER, cardNumber
					.getText().toString());
			resultData.putExtra(EXTRA_RESULT_PURCHASE_SECURIRY_NUMBER,
					securityNumber.getText().toString());
			resultData.putExtra(EXTRA_RESULT_PURCHASE_EXPIRATION, vencimiento
					.getText().toString());
			// TODO
			resultData.putExtra(EXTRA_RESULT_PURCHASE_COMPANY_ID, 1);
			setResult(MasterActivity.PURCHASE_DATA_RESULT_CODE_OK, resultData);
			finish();
		}
	}
}
