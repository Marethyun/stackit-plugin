package org.stackit.src;

import java.util.HashMap;

import org.stackit.database.DatabaseManager;

public class PurchaseManager {

	/**
	 * Get all the informations about a purchase using a purchaseId.
	 * @param String PurchaseId
	 * @return HashMap<String, Object> Informations
	 */
	public static HashMap<String, Object> getPurchase(String purchaseId) {
		return DatabaseManager.getActiveDatabase().GetPurchase(purchaseId);
	}
	
	/**
	 * Get the purchases of a user.
	 * @param String User
	 * @return HashMap<Integer, String> Purchases
	 */
	public static HashMap<Integer, String> getPurchasesInWaitingList(String user, String uuid) {
		return DatabaseManager.getActiveDatabase().GetPurchasesInWaitingList(user, uuid);
	}
	
	/**
	 * Get the arguments of a purchase.
	 * @param String PurchaseId
	 * @return HashMap<Integer, String> Purchases
	 */
	public static HashMap<String, Object> getPurchaseArguments(String purchaseId) {
		return DatabaseManager.getActiveDatabase().GetPurchaseArguments(purchaseId);
	}
	
	/**
	 * Add a user to the waiting list.
	 * @param String User
	 * @param String PurchaseId
	 */
	public static void addPurchaseToWaitingList(String user, String purchaseId) {
		DatabaseManager.getActiveDatabase().AddPurchaseToWaitingList(user, purchaseId);
	}
	
	/**
	 * Check if a purchase is already in the waiting list.
	 * @param String PurchaseId
	 * @return Boolean Is in the waiting list
	 */
	public static Boolean isPurchaseInWaitingList(String purchaseId) {
		return DatabaseManager.getActiveDatabase().IsPurchaseInWaitingList(purchaseId);
	}
	
	/**
	 * Check if a user has purchases in the waiting list.
	 * @param String User
	 * @return Boolean Has purchases in the waiting list
	 */
	public static Boolean hasPurchaseInWaitingList(String user) {
		return DatabaseManager.getActiveDatabase().HasPurchaseInWaitingList(user);
	}
	
	/**
	 * Check if a purchase has a custom message.
	 * @param String PurchaseId
	 * @return Boolean Has a custom message
	 */
	public static Boolean hasCustomMessage(String purchaseId) {
		return DatabaseManager.getActiveDatabase().HasPurchaseMessage(purchaseId);
	}
	
	/**
	 * Get the custom message of a purchase
	 * @param String PurchaseId
	 * @return String Message
	 */
	public static String getCustomMessage(String purchaseId) {
		return DatabaseManager.getActiveDatabase().GetPurchaseMessage(purchaseId);
	}
	
	/**
	 * Check if an item is a gift
	 * @param String PurchaseId
	 * @return Boolean Is a gift
	 */
	public static Boolean isAGift(String purchaseId) {
		return DatabaseManager.getActiveDatabase().IsAGift(purchaseId);
	}

}
