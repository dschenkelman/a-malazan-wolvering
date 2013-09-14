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
		this.emailAddress = email;
		this.dni = dni;
		this.birthDate = birthday;
		this.cellPhoneNumber = cellPhone;
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
	
	
}
