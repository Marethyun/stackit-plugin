package org.stackit.src;

import org.stackit.database.DatabaseManager;

public class UserManager {
	
	/**
	 * Authenticate to the API with user credentials.
	 * Generate a token.
	 * @param String User
	 * @param String Pass
	 * @return String Token
	 */
	public static String Auth(String user, String password) {
		return DatabaseManager.getActiveDatabase().AuthUser(user, password);
	}
	
	/**
	 * Create a user with a custom name and password.
	 * Default role is 1.
	 * @param String User
	 * @param String Pass
	 */
	public static void createUser(String user, String pass) {
		DatabaseManager.getActiveDatabase().CreateUser(user, pass);
	}
	
	/**
	 * Create a user with a custom name and password.
	 * Default role is 1.
	 * @param String User
	 * @param String Pass
	 * @param Integer Role
	 */
	public static void createUser(String user, String pass, Integer role) {
		DatabaseManager.getActiveDatabase().CreateUser(user, pass);
		DatabaseManager.getActiveDatabase().SetUserRole(user, role);
	}
	
	/**
	 * Get the permissions of a user.
	 * @param String User
	 * @return String Permissions
	 */
	public static String getUserPermissions(String user) {
		return DatabaseManager.getActiveDatabase().GetUserPermissions(user);
	}
}
