package phoneticket.android.model;

public class Cinema implements ICinema {

	private int id;
	private String name;
	private String address;
    private Location location;

	public Cinema(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public Cinema(int id, String name, String address, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.location = new Location(latitude, longitude);
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

	
    private static class Location implements ILocation
    {
        public double latitude;
        public double longitude;
        
    	public Location(double latitude, double longitude)
    	{
    		this.latitude = latitude;
    		this.longitude = longitude;
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

	@Override
	public ILocation getLocation() {
		return location;
	}
    
}
