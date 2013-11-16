package phoneticket.android.services.get.mock;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import phoneticket.android.model.Discount;
import phoneticket.android.services.get.IRetrieveDiscountService;
import phoneticket.android.services.get.IRetrieveDiscountsServiceDelegate;

public class MockRetrieveDiscountsService extends
		AsyncTask<String, String, String> implements IRetrieveDiscountService {
	private IRetrieveDiscountsServiceDelegate delegate;
	private List<Discount> discounts;

	@Override
	public void retrieveDiscounts(IRetrieveDiscountsServiceDelegate delegate) {
		this.delegate = delegate;
		discounts = this.getDiscounts();
		execute("");
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		delegate.retrieveDiscountsFinish(this, discounts);
		// delegate.retrieveDiscountsFinishWithError(this, 5);
	}

	private List<Discount> getDiscounts() {
		List<Discount> discounts = new ArrayList<Discount>();
		Discount discount1 = new Discount(0, "2x1 La Nacion", 0, 0.0);
		discounts.add(discount1);

		Discount discount2 = new Discount(1, "-$15 Descuento", 1, 15.0);
		discounts.add(discount2);

		Discount discount3 = new Discount(2, "25% OFF", 2, 0.25);
		discounts.add(discount3);

		return discounts;
	}

	public List<Discount> getDiscountsSent() {
		return this.discounts;
	}
}
