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

}
