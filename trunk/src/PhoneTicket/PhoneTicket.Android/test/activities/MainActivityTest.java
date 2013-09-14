package activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.activities.MainActivity;

import android.content.Intent;
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
	public void buttonShouldBeVisible() throws Exception {
		assertThat(login.getVisibility(), equalTo(View.VISIBLE));
	}
	
	@Test
	public void shouldGenerateIntentToLoginActivityClass() throws Exception {
		login.performClick();
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
		Intent intent = shadowActivity.getNextStartedActivity();
		assertNotNull(intent);
		ShadowIntent shadowIntent = Robolectric.shadowOf(intent);
		assertThat(shadowIntent.getComponent().getClassName(),
				equalTo(LoginActivity.class.getName()));
	}

}
