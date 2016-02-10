package tran.example.Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashCookies {

	public HashCookies() {
		// TODO Auto-generated constructor stub
	}
	
	public String createCookieHash(String userName) {
		String privateCookieString = returnPrivateString();
		return BCrypt.hashpw(userName+privateCookieString, BCrypt.gensalt(12));
	}
	
	public boolean validateCookieHash(String userName, String hashedValue) {
		String privateCookieString = returnPrivateString();
		if(BCrypt.checkpw(userName+privateCookieString, hashedValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private String returnPrivateString() {
		String privateCookieStringValue = "";
		Properties props = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("utility.properties");
            props.load(fis);
            privateCookieStringValue = props.getProperty("MY_PRIVATE_COOKIE_STRING");
        } catch (IOException e) {
            e.printStackTrace();
        }
		return privateCookieStringValue;
	}
}
