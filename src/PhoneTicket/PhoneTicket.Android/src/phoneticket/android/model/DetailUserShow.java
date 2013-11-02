package phoneticket.android.model;

import java.util.ArrayList;
import java.util.Collection;

public class DetailUserShow implements IDetailUserShow {
	
	private static final int TWO_PAID_ONE = 0;
	private static final int PRICE = 1;
	private static final int PERCENTAGE = 2;
	
	private String id;
	private boolean isBought;
	private int showPrice;
	private String movieTitle;
	private String showDateAndTime;
	private String complexAddress;
	private Seat[] seats;
	private Discount[] discounts;

	private class Seat implements ISeat {

		private int row;
		private int column;

		public Seat(int row, int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public int getRow() {
			return row;
		}

		@Override
		public int getColumn() {
			return column;
		}
	}

	private class Discount implements IDiscount {

		private int discountId;
		private int count;
		private double value;
		private int type;
		private String description;

		@Override
		public int getId() {
			return discountId;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public double getValue() {
			return value;
		}

		@Override
		public int getType() {
			return type;
		}

		@Override
		public String getDescription() {
			return description;
		}
	}

	public DetailUserShow(String id, boolean isBought, String movieName,
			String showTime, String complexAddress, String qrstring,
			int showPrice) {

		this.id = id;
		this.showPrice = showPrice;
		this.isBought = isBought;
		this.movieTitle = movieName;
		this.showDateAndTime = showTime;
		this.complexAddress = complexAddress;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isBought() {
		return isBought;
	}

	@Override
	public String getMovieTitle() {
		return movieTitle;
	}

	@Override
	public String getShowDateAndTime() {
		return showDateAndTime;
	}

	@Override
	public String getComplexAddress() {
		return complexAddress;
	}

	@Override
	public Collection<ISeat> getSeats() {
		Collection<ISeat> seats = new ArrayList<ISeat>();
		if (null != this.seats) {
			for (Seat seat : this.seats) {
				seats.add(seat);
			}
		}
		return seats;
	}

	@Override
	public Collection<IDiscount> getDiscounts() {
		Collection<IDiscount> discounts = new ArrayList<IDiscount>();
		if (null != this.discounts) {
			for (Discount discount : this.discounts) {
				discounts.add(discount);
			}
		}
		return discounts;
	}

	@Override
	public String getQRString() {
		return id;
	}

	@Override
	public int getShowPrice(boolean withDiscount) {
		if (withDiscount) {
			int discountPrice = showPrice * seats.length;
			if (null != discounts) {
				for (Discount discount : discounts) {
					double disc = getDiscount(discount);
					discountPrice -= disc;
				}
			}
			return discountPrice;
		} else {
			return showPrice * seats.length;
		}
	}

	private double getDiscount(Discount discount) {
		switch (discount.getType()) {
		case TWO_PAID_ONE: {
			return showPrice * discount.getCount();
		}
		case PRICE: {
			return discount.getValue() * discount.getCount();
		}
		case PERCENTAGE: {
			return showPrice * discount.getValue() * discount.getCount();
		}
		}
		return 0;
	}

	public void addSeat(int row, int column) {
		Seat seat = new Seat(row, column);
		if (null == seats) {
			seats = new Seat[1];
			seats[0] = seat;
		} else {
			Seat aux[] = new Seat[seats.length + 1];
			for (int i = 0; i < seats.length; i++) {
				aux[i] = seats[i];
			}
			aux[seats.length] = seat;
			seats = aux;
		}
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int getSingleTicketShowPrice() {
		return showPrice;
	}

	@Override
	public void setBought() {
		isBought = true;
	}

	@Override
	public int getShowDuration() {
		return 90;
	}
}
