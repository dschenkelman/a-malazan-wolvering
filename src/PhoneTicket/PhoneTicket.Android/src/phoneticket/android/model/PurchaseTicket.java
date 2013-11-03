package phoneticket.android.model;

import java.util.List;

public class PurchaseTicket {
	private int showId;
	private List<PostedArmChair> armChairs;

	private String creditCardNumber;
	private String creditCardSecurityCode;
	private String creditCardExpiration;
	private int creditCardCompanyId;

	public PurchaseTicket(int showId, List<PostedArmChair> armChairs,
			String creditCardNumber, String creditCardSecurityCode,
			String creditCardExpirationDate, int creditCardCompanyId) {
		super();
		this.showId = showId;
		this.armChairs = armChairs;
		this.creditCardNumber = creditCardNumber;
		this.creditCardSecurityCode = creditCardSecurityCode;
		this.creditCardExpiration = creditCardExpirationDate;
		this.creditCardCompanyId = creditCardCompanyId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getCreditCardSecurityCode() {
		return creditCardSecurityCode;
	}

	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}

	public int getCreditCardCompanyId() {
		return creditCardCompanyId;
	}

	public int getShowId() {
		return showId;
	}

	public List<PostedArmChair> getArmChairs() {
		return armChairs;
	}

}
