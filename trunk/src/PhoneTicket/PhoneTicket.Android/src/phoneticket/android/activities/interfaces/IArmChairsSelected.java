package phoneticket.android.activities.interfaces;

import java.util.List;

import phoneticket.android.model.ArmChair;
import phoneticket.android.model.Ticket;

public interface IArmChairsSelected {
	public void onArmChairsSelected(List<ArmChair> armChairs, Ticket ticket);
}
