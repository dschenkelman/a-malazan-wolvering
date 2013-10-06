package phoneticket.android.model;

public class Location implements ILocation {
	public double longitude;
	public double latitude;

	public Location(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

}
