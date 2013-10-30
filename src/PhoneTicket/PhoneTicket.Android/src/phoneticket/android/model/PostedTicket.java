package phoneticket.android.model;

import java.util.List;

public class PostedTicket {
	private int showId;
	private List<PostedArmChair> armChairs;

	public PostedTicket(int showId, List<PostedArmChair> armChairs) {
		super();
		this.showId = showId;
		this.armChairs = armChairs;
	}

	public int getShowId() {
		return showId;
	}

	public List<PostedArmChair> getArmChairs() {
		return armChairs;
	}

}
