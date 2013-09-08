package phoneticket.android.model;

public class User implements IUser {

	private String firstName;
	private String lastName;
	private String email;
	private int dni;
	private String birthday;
	private String phone;
	private String cellPhone;
	private String password;
	
	public User(String name, String lastName, String email, int dni, 
			String birthday, String phone, String cellPhone, String password) {
		firstName = name;
		this.lastName = lastName;
		this.email = email;
		this.dni = dni;
		this.birthday = birthday;
		this.phone = phone;
		this.cellPhone = cellPhone;
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
		return email;
	}

	@Override
	public int getDni() {
		return dni;
	}

	@Override
	public String getBirthday() {
		return birthday;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public String getCellPhone() {
		return cellPhone;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	
}
