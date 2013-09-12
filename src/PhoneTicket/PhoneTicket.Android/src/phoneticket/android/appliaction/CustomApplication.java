package phoneticket.android.appliaction;

import roboguice.RoboGuice;
import android.app.Application;

public class CustomApplication extends Application{
	@Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE, 
            RoboGuice.newDefaultRoboModule(this), new PhoneTicketModule());
    }

}
