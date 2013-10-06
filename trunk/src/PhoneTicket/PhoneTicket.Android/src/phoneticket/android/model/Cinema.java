package phoneticket.android.model;

public class Cinema implements ICinema {

	private int id;
	private String name;
	private String address;
	private ILocation location;

	public Cinema(int id, String name, String address, ILocation location) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.location = location;
	}

	@Override
	public double getLongitude() {
		return location.getLongitude();
	}

	@Override
	public double getLatitude() {
		return location.getLatitude();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAddress() {
		return address;
	}

}
