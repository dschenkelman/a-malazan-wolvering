package phoneticket.android.model;

import java.util.ArrayList;
import java.util.Collection;

public class DetailUserShow implements IDetailUserShow {

	private int id;
	private boolean isBought;
	private int showPrice;
	private String movieTitle;
	private String showDateAndTime;
	private String complexAddress;
	private Seat[] seats;
	private Discount[] discounts;
	private String qrstring;

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

		private int id;
		private int count;

		@Override
		public int getId() {
			return id;
		}

		@Override
		public int getCount() {
			return count;
		}
	}

	public DetailUserShow(int id, boolean isBought, String movieName,
			String showTime, String complexAddress, String qrstring,
			int showPrice) {

		this.id = id;
		this.showPrice = showPrice;
		this.isBought = isBought;
		this.movieTitle = movieName;
		this.showDateAndTime = showTime;
		this.complexAddress = complexAddress;
		this.qrstring = qrstring;
	}

	@Override
	public int getId() {
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
		return qrstring;
	}

	@Override
	public int getShowPrice(boolean withDiscount) {
		if (withDiscount) {
			int discountPrice = showPrice;
			// TODO get discount
			return discountPrice;
		} else {
			return showPrice;
		}
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
}
