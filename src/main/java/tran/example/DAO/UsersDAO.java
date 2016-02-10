package tran.example.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Allows interaction with the Users Table in the DB. 
 * @author Todd
 *
 */
public class UsersDAO {

	private Connection dbConn;

	public UsersDAO(Connection dbConn) {
		this.dbConn = dbConn;
	}
	
	public boolean checkUserPW(String userName, String password) throws SQLException {
		String hashedPW = getHashedPassword(userName);
		if(hashedPW.equals("")) // default value so the password could not be retrieved.
			return false;
		else {
			// checking if this guessed password matches with the stored password retrieved from the database.
			if(BCrypt.checkpw(password, hashedPW))
				return true;
			else {
				return false; // incorrect password
			}
		}
	}
	
	public int createUser(String userName, String password) throws SQLException {
		int userCreated = 0; // default assumes no rows are affected (no insertions are made)
		if(dbConn != null) {
			String hashedPassword = createPassword(password);
			String insertUser = "INSERT into Users(user_Name, user_Password) VALUES (?, ?)";
			PreparedStatement insertUserStatement = dbConn.prepareStatement(insertUser);
			insertUserStatement.setString(1, userName);
			insertUserStatement.setString(2, hashedPassword);
			userCreated = insertUserStatement.executeUpdate();
			insertUserStatement.close();
		}
		return userCreated;
	}
	
	public String createPassword(String password) {
		// 2 ^ 14 rounds of hashing.
		String hashedPW = BCrypt.hashpw(password, BCrypt.gensalt(14));
		return hashedPW;
	}
	
	public String getHashedPassword(String userName) throws SQLException {
		String hashedPW = "";
		if(dbConn != null) {
			
			String getUserPassword = "SELECT user_Password FROM Users WHERE user_Name = ?";
			PreparedStatement queries = dbConn.prepareStatement(getUserPassword);
			queries.setString(1, userName);

			ResultSet resultUser = queries.executeQuery();

			while(resultUser.next()) {
				hashedPW = resultUser.getString(1);
			}
			queries.close();
		}
		return hashedPW;
	}
	
	public int modifyPassword(String userName, String newPassword) throws SQLException {
		int returnCodeValue = 0;
		int testForDuplicatePassword = returnOnOldPassword(userName, newPassword);
		if(dbConn != null && testForDuplicatePassword > 0) {
			String newHashedPassword = createPassword(newPassword);
			String changeUserPasswordSQL = "UPDATE Users SET user_Password = ? WHERE user_Name = ?";
			PreparedStatement updateStatement = dbConn.prepareStatement(changeUserPasswordSQL);
			updateStatement.setString(1, newHashedPassword);
			updateStatement.setString(2, userName);
			returnCodeValue = updateStatement.executeUpdate();
			updateStatement.close();
		}
		return returnCodeValue;
	}
	
	// helper method to see if the newPassword is identical to the old one.
	public int returnOnOldPassword(String userName, String newPassword) throws SQLException {
		String currentlyStoredPassword = getHashedPassword(userName);
		if(BCrypt.checkpw(newPassword, currentlyStoredPassword))
			return 0;
		else 
			return 1;
		
	}
}
