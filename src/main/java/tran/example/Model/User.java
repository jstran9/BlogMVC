package tran.example.Model;

/**
 * This class will help with server side validation when user submits to a form.
 * @author Todd
 */
public class User {
	// by default these fields are all empty.
	private String userName = "";
	private String password = "";
	private String validatePassword = "";
	
	private String message = "";

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String password, String validatePassword) {
		this.password = password;
		this.validatePassword = validatePassword;
	}
	
	public User(String userName, String password, String validatePassword) {
		super();
		this.userName = userName;
		this.password = password;
		this.validatePassword = validatePassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMessage() {
		return message;
	}
	
	public boolean validate() {
		String userNameRegex = "^[a-z0-9_-]{6,35}$";
		String passwordRegex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})";
		
		if(!userName.matches(userNameRegex)) {
			message = "The user name must be at least 6 characters long and up to 35 characters.\nOnly lower case letters, numbers, an underscore , or hyphen are allowed!";
			return false;
		}
		
		if(!password.matches(passwordRegex)) {
			message = "The password must have at least one number, one lower and upper case letter, and one of the special symbols: '@', '#', '$', '%'.\nThe length must be between 6 to 20 characters.";
			return false;
		}
		if(!password.equals(validatePassword)) {
			message = "The entered passwords must match, try again!";
			return false;
		}
				
		return true;
	}

	public boolean validatePasswords() {
		String passwordRegex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})";
		
		if(!password.matches(passwordRegex)) {
			message = "The password must have at least one number, one lower and upper case letter, and one of the special symbols: '@', '#', '$', '%'.\nThe length must be between 6 to 20 characters.";
			return false;
		}
		if(!password.equals(validatePassword)) {
			message = "The entered passwords must match, try again!";
			return false;
		}
				
		return true;
	}
}
