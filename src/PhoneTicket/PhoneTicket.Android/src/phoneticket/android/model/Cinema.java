package phoneticket.android.model;

public class Cinema implements ICinema {

	private int id;
    private String name;
    private String address;
    private Location location;
    
    private static class Location
    {
        public double longitude;
        public double latitude;
        
    	@SuppressWarnings("unused")
		public Location(double longitude, double latitude)
    	{
    		this.longitude = longitude;
    		this.latitude = latitude;
    	}
    }
    
	@Override
	public double getLongitude()
	{
		return location.longitude;
	}

	@Override
	public double getLatitude()
	{
		return location.latitude;
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
