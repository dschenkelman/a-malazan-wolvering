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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((armChairs == null) ? 0 : armChairs.hashCode());
		result = prime * result + creditCardCompanyId;
		result = prime
				* result
				+ ((creditCardExpiration == null) ? 0 : creditCardExpiration
						.hashCode());
		result = prime
				* result
				+ ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime
				* result
				+ ((creditCardSecurityCode == null) ? 0
						: creditCardSecurityCode.hashCode());
		result = prime * result + showId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchaseTicket other = (PurchaseTicket) obj;
		if (armChairs == null) {
			if (other.armChairs != null)
				return false;
		} else if (!(armChairs.size() == other.armChairs.size()))
			return false;
		if (creditCardCompanyId != other.creditCardCompanyId)
			return false;
		if (creditCardExpiration == null) {
			if (other.creditCardExpiration != null)
				return false;
		} else if (!creditCardExpiration.equals(other.creditCardExpiration))
			return false;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (creditCardSecurityCode == null) {
			if (other.creditCardSecurityCode != null)
				return false;
		} else if (!creditCardSecurityCode.equals(other.creditCardSecurityCode))
			return false;
		if (showId != other.showId)
			return false;
		return true;
	}

}
