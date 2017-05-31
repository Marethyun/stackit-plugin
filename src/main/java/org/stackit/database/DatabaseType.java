package org.stackit.database;

import java.util.HashMap;

public interface DatabaseType {
	/**
	 * Return the database.
	 * @return Object
	 */
	public Object getDatabase();
	
	/**
	 * Function used to connect to the database. Require to return the object
	 * when/if connected.
	 * @return Object Database connection
	 */
	public Object Connect();
	
	/**
	 * Check and verify the database.
	 * Create the tables if they do not exist
	 */
	public void CheckDatabase();
	
	/**
	 * Get context information.
	 * @param String Context
	 * @return Object Context
	 */
	public HashMap<String, Object> GetContext(String context);
	
	/**
	 * Get role information.
	 * @param String Role
	 * @return Object Role
	 */
	public HashMap<String, Object> GetRole(Integer role);
	
	/**
	 * Get token information.
	 * @param String Token
	 * @return Object Token
	 */
	public HashMap<String, Object> GetToken(String token);

	/**
	 * Get user information.
	 * @param String User
	 * @return Object User
	 */
	public HashMap<String, Object> GetUser(String user);
	
	/**
	 * Get all the informations about a purchase using a purchaseId.
	 * @param String PurchaseId
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> GetPurchase(String purchaseId);
	
	/**
	 * Connect a user to the API with credentials.
	 * @param String User
	 * @param String Pass
	 * @return String Authentication token
	 */
	public String AuthUser(String user, String pass);
	
	/**
	 * Connect a user to the API with a token.
	 * @param String User
	 * @param String Pass
	 * @return String Authentication token
	 */
	public String AuthToken(String token);
	
	/**
	 * Add a log to the database
	 */
	public void AddLog(String date, String time, String log, String executor, String context, String remoteAddress);
	
	/**
	 * Add a buy to the waiting list.
	 * @param String User
	 * @param String ID - Buy_id (Command ID)
	 */
	public void AddPurchaseToWaitingList(String user, String buy_id);
	
	/**
	 * Check if a user has items in the waiting list.
	 * @param String User
	 * @return Boolean Is present
	 */
	public Boolean HasPurchaseInWaitingList(String user);
	
	/**
	 * Check if there is a custom message attached to the purchase.
	 * @param String User
	 * @return Boolean Is present
	 */
	public Boolean HasPurchaseMessage(String purchaseId);
	
	/**
	 * Check if a purchase is a gift
	 * @param String PurchaseId
	 * @return Boolean Is gift
	 */
	public Boolean IsAGift(String purchaseId);
	
	/**
	 * Get the custom message of a purchase.
	 * @param String User
	 * @return String Custom message
	 */
	public String GetPurchaseMessage(String purchaseId);
	
	/**
	 * Check if a command is already in the waiting list.
	 * @param String PurchaseId
	 * @return Boolean Is present
	 */
	public Boolean IsPurchaseInWaitingList(String purchaseId);
	
	/**
	 * Get all the ids of the buys of a user.
	 * @param String User
	 * @param String UUID
	 */
	public HashMap<Integer, String> GetPurchasesInWaitingList(String user, String uuid);
	
	/**
	 * Get the arguments of a purchase
	 * @param String PurchaseId
	 * @return HashMap<String, Object> Arguments
	 */
	public HashMap<String, Object> GetPurchaseArguments(String purchaseId);
	
	/**
	 * Remove a purchase from buys and buys_waiting and put it in buys_history
	 * @param String PurchaseId
	 */
	public void RemovePurchase(String purchaseId);
	
	/**
	 * Check if context exists.
	 * @param String Context
	 * @return Boolean Context exists
	 */
	public Boolean ContextExist(String context);
	
	/**
	 * Check if user exists.
	 * @param String User
	 * @return Boolean User exists
	 */
	public Boolean UserExist(String user);
	
	/**
	 * Generate a session token for the API.
	 * @param String User
	 * @return String Generated token
	 */
	public String GenerateToken(String user);
	
	/**
	 * Get the permission required for a context.
	 * @param String Context
	 * @return String Required permission
	 */
	public String GetContextRequiredPermission(String context);
	
	/**
	 * Get the permissions of a role.
	 * @param Integer Role
	 * @return String Permissions
	 */
	public String GetRolePermissions(Integer role);
	
	/**
	 * Check if token exists.
	 * @param String Token
	 * @return Boolean Token exists
	 */
	public Boolean TokenExist(String token);
	
	/**
	 * Get the user of a token.
	 * @param String Token
	 * @return String User
	 */
	public String GetTokenUser(String token);
	
	/**
	 * Get the token expiration date.
	 * @param String Token
	 * @return String Permissions
	 */
	public String GetTokenExpiration(String token);
	
	/**
	 * Get the permissions of a token.
	 * @param String Token
	 * @return String Permissions
	 */
	public String GetTokenPermissions(String token);
	
	/**
	 * Set the required permission for a context in the database.
	 * @param String Context
	 * @param String Required permission
	 */
	public void CreateContext(String context, String permission);
	
	/**
	 * Create a user.
	 * @param String User
	 * @param String Pass
	 */
	public String CreateUser(String user, String pass);
	
	/**
	 * Delete a user.
	 * @param String User
	 */
	public String DeleteUser(String user);
	
	/**
	 * Get the permissions of a user.
	 * @param String User
	 * @return String Permissions
	 */
	public String GetUserPermissions(String user);
	
	/**
	 * Set the role of a user.
	 * @param String User
	 * @param String Role
	 */
	public void SetUserRole(String user, Integer role);
	
	/**
	 * Stop the database connection.
	 */
	public void Disconnect();
}
