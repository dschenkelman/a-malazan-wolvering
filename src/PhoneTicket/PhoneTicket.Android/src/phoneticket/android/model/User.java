package phoneticket.android.model;

public class User implements IUser {

	private String firstName;
	private String lastName;
	private String emailAddress;
	private int id;
	private String birthday;
	private String cellPhoneNumber;
	private String password;

	public User(String name, String lastName, String email, int dni,
			String birthday, String cellPhone, String password) {
		firstName = name;
		this.lastName = lastName;
		emailAddress = email;
		this.id = dni;
		this.birthday = birthday;
		this.cellPhoneNumber = cellPhone;
		this.password = password;
	}

	public User(int id, String email, String password) {
		firstName = "";
		lastName = "";
		emailAddress = email;
		this.id = id;
		birthday = "";
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
		return id;
	}

	@Override
	public String getBirthday() {
		return birthday;
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
				+ ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result
				+ ((cellPhoneNumber == null) ? 0 : cellPhoneNumber.hashCode());
		result = prime * result + id;
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
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (cellPhoneNumber == null) {
			if (other.cellPhoneNumber != null)
				return false;
		} else if (!cellPhoneNumber.equals(other.cellPhoneNumber))
			return false;
		if (id != other.id)
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
		return "Usuario: \n Email: " + emailAddress;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setCellPhone(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}
}
