package phoneticket.android.activities.interfaces;

import phoneticket.android.model.DiscountCountable;

public interface IDiscountSelectedListener {

	public boolean discountIncrease(DiscountCountable discount);

	public void discountDecrease(DiscountCountable discount);

	public void discountUnSelected(DiscountCountable discount);

}
