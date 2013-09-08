import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.view.View;
import android.widget.Button;

import com.example.roboelectric.Home;
import com.example.roboelectric.MainActivity;
import com.example.roboelectric.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.matchers.StartedMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MyActivityTest {

 private MainActivity activity;
 private Button login;

 @Before
 public void setUp() throws Exception {
  activity = new MainActivity();
  activity.onCreate(null);
  login = (Button) activity.findViewById(R.id.login);
 }

//checking hello world text
@Test
public void shouldHaveHappySmiles() throws Exception {
String hello = new MainActivity().getResources().getString(
  R.string.hello_world);
assertThat(hello, equalTo("Hello world!"));
}

// Button visible
@Test
public void testButtonsVisible() {
assertThat(login.getVisibility(), equalTo(View.VISIBLE));
}

// startnew activty
@Test
public void shouldStartNextActivityWhenButtonIsClicked() {
login.performClick();
assertThat(activity, new StartedMatcher(Home.class));
}
 
}