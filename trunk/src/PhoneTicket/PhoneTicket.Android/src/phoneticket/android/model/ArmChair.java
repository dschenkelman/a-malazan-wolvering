package phoneticket.android.model;

public class ArmChair {

	public static final int OCUPADA = 1;
	public static final int LIBRE = 2;
	public static final int SELECCIONADA = 3;

	private static final int INEXISTENTE = 0;
	private int state;
	private int column;
	private String row;

	public ArmChair(int state, int column, String row) {
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

	public String getRow() {
		return row;
	}

	public void liberar() {
		this.state = LIBRE;
	}

	public boolean libre() {
		return state == LIBRE;
	}

	public void seleccionar() {
		this.state = SELECCIONADA;
	}

	public boolean seleccionada() {
		return state == SELECCIONADA;
	}

	public boolean existente() {
		return state != INEXISTENTE;
	}

}
