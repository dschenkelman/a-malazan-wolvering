package phoneticket.android.model;

public class PostedDiscounts {

	private int discountId;
	private int count;

	public PostedDiscounts(int discountId, int count) {
		super();
		this.discountId = discountId;
		this.count = count;
	}

	public int getDiscountId() {
		return discountId;
	}

	public int getCount() {
		return count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + discountId;
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
		PostedDiscounts other = (PostedDiscounts) obj;
		if (count != other.count)
			return false;
		if (discountId != other.discountId)
			return false;
		return true;
	}

}
