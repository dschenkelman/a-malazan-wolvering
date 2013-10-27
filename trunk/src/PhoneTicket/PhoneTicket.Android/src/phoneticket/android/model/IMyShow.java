package phoneticket.android.model;

import java.io.Serializable;

public interface IMyShow extends Serializable {

	int getId();

	boolean isBought();

	String getMovieName();

	String getShowTime();

	String getComplexAddress();

}
