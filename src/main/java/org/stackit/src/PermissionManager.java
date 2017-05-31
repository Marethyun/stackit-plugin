package org.stackit.src;

import org.stackit.database.Database;

public class PermissionManager {
	
	/**
	 * Create a context with permissions.
	 * @param String Context
	 * @param String Permission
	 */
	public static void createContext(String context, String permission) {
		Database.getActiveDatabase().CreateContext(context, permission);
	}
	
	/**
	 * Check if context has permissions.
	 * @param String Context
	 */
	public static Boolean contextHasPermissions(String context) {
		return Database.getActiveDatabase().ContextExist(context);
	}
	
	/**
	 * Get the required permission for a context
	 * @param String Context
	 * @return String Required permission
	 */
	public static String getRequiredPermissionForContext(String context) {
		return Database.getActiveDatabase().GetContextRequiredPermission(context);
	}
	
	/**
	 * Check if user has all the permissions.
	 * @param String User
	 */
	public static Boolean userHasAllPermissions(String user) {
		String userPermissions = Database.getActiveDatabase().GetUserPermissions(user);
		
		if(userPermissions.contains("*")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if token has all the permissions.
	 * @param String User
	 */
	public static Boolean tokenHasAllPermissions(String user) {
		String tokenPermissions = Database.getActiveDatabase().GetTokenPermissions(user);
		
		if(tokenPermissions.contains("*")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if a user has enough permissions to execute a context
	 * @param String Context
	 * @param String User
	 * @return Boolean Enough permissions
	 */
	public static Boolean userHasEnoughPermissionsForContext(String context, String user){
		String userPermissions = Database.getActiveDatabase().GetUserPermissions(user);
		
		if(userPermissions.contains(getRequiredPermissionForContext(context)) || userHasAllPermissions(user)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if a user has enough permissions to execute a context
	 * @param String Context
	 * @param String User
	 * @return Boolean Enough permissions
	 */
	public static Boolean tokenHasEnoughPermissionsForContext(String context, String token){
		String tokenPermissions = Database.getActiveDatabase().GetTokenPermissions(token);
		
		if(tokenPermissions.contains(getRequiredPermissionForContext(context)) || tokenHasAllPermissions(token)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the required permissions for a context.
	 * @param String Context
	 * @return String Required permissions
	 */
	public static String getContextRequiredPermission(String context) {
		return Database.getActiveDatabase().GetContextRequiredPermission(context);
	}

}
