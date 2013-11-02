package phoneticket.android.model;

public class CreditCardData {

	private String creditCardNumber;
	private String creditCardSecurityCode;
	private int creditCardCompanyId;
	private String creditCardExpiration;

	public CreditCardData(String cardNumber, String securityCode,
			int company, String expiration) {
		creditCardNumber = cardNumber;
		creditCardSecurityCode = securityCode;
		creditCardCompanyId = company;
		creditCardExpiration = expiration;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getCreditCardSecurityCode() {
		return creditCardSecurityCode;
	}

	public int getCreditCardCompanyId() {
		return creditCardCompanyId;
	}

	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}
}
