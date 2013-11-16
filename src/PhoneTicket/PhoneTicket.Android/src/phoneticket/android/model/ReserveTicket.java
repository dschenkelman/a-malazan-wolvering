package phoneticket.android.model;

import java.util.List;

public class ReserveTicket {
	private int showId;
	private List<PostedArmChair> armChairs;

	public ReserveTicket(int showId, List<PostedArmChair> armChairs) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((armChairs == null) ? 0 : armChairs.hashCode());
		result = prime * result + showId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReserveTicket other = (ReserveTicket) obj;
		if (armChairs == null) {
			if (other.armChairs != null)
				return false;
		} else if (!(armChairs.size() == other.armChairs.size()))
			return false;
		if (showId != other.showId)
			return false;
		return true;
	}

}
