package phoneticket.android.model;

public class Discount {

	private int id;
	private String description;
	private int type;
	private double value;

	public Discount(int id, String description, int type, double value) {
		super();
		this.id = id;
		this.description = description;
		this.type = type;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public int getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

}
