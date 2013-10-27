package phoneticket.android.model;

public class Function implements IFunction {

	private int id;
	private String day;
	private String time;
	private int roomId;
	private String completeDay;

	public Function(int id, String day, String time, int roomId,
			String completeDay) {
		this.id = id;
		this.day = day;
		this.time = time;
		this.roomId = roomId;
		this.completeDay = completeDay;
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

	@Override
	public int getRoomId() {
		return roomId;
	}

	@Override
	public String toString() {
		return time;
	}

	@Override
	public String getCompleteDay() {
		return this.completeDay;
	}

}
