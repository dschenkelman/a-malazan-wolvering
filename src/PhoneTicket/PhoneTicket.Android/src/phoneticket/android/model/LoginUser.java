package phoneticket.android.model;

public class LoginUser {

	private String emailAddress;
	private String password;
	
	public LoginUser(String email, String password) {
		this.emailAddress = email;
		this.password = password;
	}

	public String getEmail() {
		return emailAddress;
	}
	
	public String getPassword() {
		return password;
	}

	
	@Override
	public String toString() {
		return "LoginUser [email=" + emailAddress + ", password=" + password + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
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
		LoginUser other = (LoginUser) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
	
}
