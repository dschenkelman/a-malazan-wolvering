package activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


import phoneticket.android.R;
import phoneticket.android.activities.MainActivity;

import android.view.View;
import android.widget.Button;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	private MainActivity activity;
	private Button login;
	
	@Before
    public void setUp() throws Exception {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
        login = (Button) activity.findViewById(R.id.actionButton);
    }
	
	@Test
	public void buttonShouldBeVisible() throws Exception{
		assertThat(login.getVisibility(), equalTo(View.VISIBLE));
	}
	
}
