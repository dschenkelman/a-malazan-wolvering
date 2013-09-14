package phoneticket.android.model;

public class User implements IUser {

	private String firstName;
	private String lastName;
	private String emailAddress;
	private int dni;
	private String birthDate;
	private String cellPhoneNumber;
	private String password;

	public User(String name, String lastName, String email, int dni,
			String birthday, String cellPhone, String password) {
		firstName = name;
		this.lastName = lastName;
		emailAddress = email;
		this.dni = dni;
		this.birthDate = birthday;
		this.cellPhoneNumber = cellPhone;
		this.password = password;
	}

	public User(int id, String email, String password) {
		firstName = "";
		lastName = "";
		emailAddress = email;
		dni = id;
		birthDate = "";
		cellPhoneNumber = "";
		this.password = password;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getEmail() {
		return emailAddress;
	}

	@Override
	public int getDni() {
		return dni;
	}

	@Override
	public String getBirthday() {
		return birthDate;
	}

	@Override
	public String getCellPhone() {
		return cellPhoneNumber;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result
				+ ((cellPhoneNumber == null) ? 0 : cellPhoneNumber.hashCode());
		result = prime * result + dni;
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (cellPhoneNumber == null) {
			if (other.cellPhoneNumber != null)
				return false;
		} else if (!cellPhoneNumber.equals(other.cellPhoneNumber))
			return false;
		if (dni != other.dni)
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", dni=" + dni
				+ ", birthDate=" + birthDate + ", cellPhoneNumber="
				+ cellPhoneNumber + ", password=" + password + "]";
	}
}
