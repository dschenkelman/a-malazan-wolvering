package phoneticket.android.model;

public class Function implements IFunction {

	private int id;
	private String day;
	private String time;

	public Function(int id, String day, String time) {
		this.id = id;
		this.day = day;
		this.time = time;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getDay() {
		return day;
	}

	@Override
	public String getTime() {
		return time;
	}

}
