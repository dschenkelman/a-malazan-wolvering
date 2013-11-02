package phoneticket.android.model;

import java.util.Date;
import java.util.List;

public class PurchaseTicket {
	private int showId;
	private List<PostedArmChair> armChairs;

	private String creditCardNumber;
	private String creditCardSecurityCode;
	private Date creditCardExpirationDate;
	private int creditCardCompanyId;

	public PurchaseTicket(int showId, List<PostedArmChair> armChairs,
			String creditCardNumber, String creditCardSecurityCode,
			Date creditCardExpirationDate, int creditCardCompanyId) {
		super();
		this.showId = showId;
		this.armChairs = armChairs;
		this.creditCardNumber = creditCardNumber;
		this.creditCardSecurityCode = creditCardSecurityCode;
		this.creditCardExpirationDate = creditCardExpirationDate;
		this.creditCardCompanyId = creditCardCompanyId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getCreditCardSecurityCode() {
		return creditCardSecurityCode;
	}

	public Date getCreditCardExpirationDate() {
		return creditCardExpirationDate;
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
