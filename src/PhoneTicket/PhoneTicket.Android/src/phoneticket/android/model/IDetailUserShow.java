package phoneticket.android.model;

import java.util.Collection;

public interface IDetailUserShow extends IMyShow {

	Collection<ISeat> getSeats();
	
	Collection<IDiscount> getDiscounts();

	String getQRString();

	int getShowPrice(boolean whitDiscount);

	void setId(String showId);

}
