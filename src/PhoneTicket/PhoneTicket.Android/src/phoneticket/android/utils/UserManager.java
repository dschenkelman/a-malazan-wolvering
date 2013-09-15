package phoneticket.android.utils;

import android.content.SharedPreferences;
import phoneticket.android.model.User;

public class UserManager {

	private static UserManager instance;
	private User user;
	private String emailCredential;
	private String passwordCredential;
	private SharedPreferences sharePreferences;
	final private String kSharePreferencesEmailAddressCredential = "shared.user.credential.emailaddress";
	final private String kSharePreferencesPasswordCredential = "shared.user.credential.password";
	final private String kSharePreferencesUserIdCredential = "shared.user.id";
	final private int InvalidId = -1;
	
	public static void initialize(SharedPreferences sharePreferences) {
		instance = new UserManager(sharePreferences);
	}

	public static UserManager getInstance() {
		return instance;
	}

	private UserManager(SharedPreferences sharePreferences) {
		this.sharePreferences = sharePreferences;
		int id = sharePreferences.getInt(
				kSharePreferencesUserIdCredential, InvalidId);
		if(InvalidId != id) {
			emailCredential = sharePreferences.getString(
					kSharePreferencesEmailAddressCredential, "");
			passwordCredential = sharePreferences.getString(
					kSharePreferencesPasswordCredential, "");
			user = new User(id, emailCredential, passwordCredential);
		}
		user = null;
	}

	public void setCredentials(String emailAddress, String password) {
		this.emailCredential = emailAddress;
		this.passwordCredential = password;
	}

	public String getEmailAddressCredential() {
		return null == this.emailCredential ? "" : this.emailCredential;
	}

	public String getPasswordCredential() {
		return null == this.passwordCredential ? "" : this.passwordCredential;
	}

	public User getLogedUser() {
		return user;
	}

	public void loginUserWithId(int id, boolean usingCredentials) {
		user = new User(id, emailCredential, passwordCredential);
		SharedPreferences.Editor editor = sharePreferences.edit();
		editor.putInt(kSharePreferencesUserIdCredential, id);
		editor.putString(kSharePreferencesEmailAddressCredential,
				emailCredential);
		editor.putString(kSharePreferencesPasswordCredential,
				passwordCredential);
		editor.commit();
	}
	
	public void logoutUser() {
		user = null;
		emailCredential = "";
		passwordCredential = "";
		SharedPreferences.Editor editor = sharePreferences.edit();
		editor.remove(kSharePreferencesUserIdCredential);
		editor.remove(kSharePreferencesEmailAddressCredential);
		editor.remove(kSharePreferencesPasswordCredential);
		editor.commit();
	}

	public boolean isUserLoged() {
		return null != user;
	}
}
