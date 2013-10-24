package phoneticket.android.model;

public interface ICinema {

	int getId();

	String getName();

	String getAddress();

	double getLongitude();

	double getLatitude();

	ILocation getLocation();
}
