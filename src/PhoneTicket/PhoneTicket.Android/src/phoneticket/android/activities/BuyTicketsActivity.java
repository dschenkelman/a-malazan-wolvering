package phoneticket.android.activities;

import phoneticket.android.R;
import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.view.Menu;

public class BuyTicketsActivity extends RoboFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_tickets);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_tickets, menu);
		return true;
	}

}
