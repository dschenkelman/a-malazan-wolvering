package phoneticket.android.model;

public class DiscountCountable extends Discount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int count;
	private boolean selected;

	public DiscountCountable(Discount discount) {
		super(discount.getId(), discount.getDescription(), discount.getType(),
				discount.getValue());
		this.count = 0;
		this.selected = false;
	}

	public int getCount() {
		return count;
	}

	public void plusCount() {
		this.count++;
	}

	public void decreaseCount() {
		this.count--;
		if (count < 0)
			clearCount();
	}

	public void select(boolean select) {
		this.selected = select;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void clearCount() {
		this.count = 0;
	}

}
