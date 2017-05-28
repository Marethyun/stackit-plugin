package org.stackit.src;

import org.stackit.dtb.Database;

public class UserManager {
	
	/**
	 * Authenticate to the API with user credentials.
	 * Generate a token.
	 * @param String User
	 * @param String Pass
	 * @return String Token
	 */
	public static String Auth(String user, String password) {
		return Database.getActiveDatabase().AuthUser(user, password);
	}
	
	/**
	 * Create a user with a custom name and password.
	 * Default role is 1.
	 * @param String User
	 * @param String Pass
	 */
	public static void createUser(String user, String pass) {
		Database.getActiveDatabase().CreateUser(user, pass);
	}
	
	/**
	 * Create a user with a custom name and password.
	 * Default role is 1.
	 * @param String User
	 * @param String Pass
	 * @param Integer Role
	 */
	public static void createUser(String user, String pass, Integer role) {
		Database.getActiveDatabase().CreateUser(user, pass);
		Database.getActiveDatabase().SetUserRole(user, role);
	}
	
	/**
	 * Get the permissions of a user.
	 * @param String User
	 * @return String Permissions
	 */
	public static String getUserPermissions(String user) {
		return Database.getActiveDatabase().GetUserPermissions(user);
	}
}
