package phoneticket.android.utils;

import phoneticket.android.model.User;

public class UserManager {
	
	private static UserManager instance;
	private User user;
	private String emailCredential;
	private String passwordCredential;
	
	public static UserManager getInstance() {
		if(null == instance) {
			instance = new UserManager();
		}
		return instance;
	}
	
	private UserManager() {
		emailCredential = "";
		passwordCredential = "";
		user = null;
	}
	
	public void setCredentials(String emailAddress, String password) {
		this.emailCredential = emailAddress;
		this.passwordCredential = password;
	}
	
	public String getEmailAddressCredential() {
		return this.emailCredential;
	}
	
	public String getPasswordCredential() {
		return this.passwordCredential;
	}
	
	public User getLogedUser() {
		return user;
	}

	public void createUserWithId(int id, boolean usingCredentials) {
		user = new User(id, emailCredential, passwordCredential);
	}
}
