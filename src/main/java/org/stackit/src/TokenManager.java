package org.stackit.src;

import org.stackit.dtb.Database;

public class TokenManager {
	
	/**
	 * Authenticate to the system with a token
	 * @param String Token
	 */
	public static String Auth(String token) {
		return Database.getActiveDatabase().AuthToken(token);
	}
	
	/**
	 * Create a token with the username and the password of a user.
	 * @param String user
	 * @param String pass
	 * @return String token
	 */
	public static String createToken(String user, String pass) {
		return Database.getActiveDatabase().AuthUser(user, pass);
	}
	
	/**
	 * Check if the given API token exists.
	 * @param String (the token id)
	 * @return Boolean
	 */
	public static Boolean tokenExist(String token) {
		return Database.getActiveDatabase().TokenExist(token);
	}
	
	/**
	 * Get the permissions of a role.
	 * @param String (the role id)
	 * @return String
	 */
	public static String getRolePermissions(Integer role) {
		return Database.getActiveDatabase().GetRolePermissions(role);
	}
	
	/**
	 * Get the permissions of a token
	 * @param String (the token id)
	 * @return String
	 */
	public static String getTokenPermissions(String token) {
		return Database.getActiveDatabase().GetTokenPermissions(token);
	}
	
	/**
	 * Get the user of a token
	 * @param String Token
	 * @return String User
	 */
	public static String getTokenUser(String token) {
		return Database.getActiveDatabase().GetTokenUser(token);
	}

}
