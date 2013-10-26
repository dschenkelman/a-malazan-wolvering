package phoneticket.android.model;

public class ArmChair {

	private int state;
	private int column;
	private int row;

	public ArmChair(int state, int column, int row) {
		this.state = state;
		this.column = column;
		this.row = row;
	}

	public int getState() {
		return state;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
