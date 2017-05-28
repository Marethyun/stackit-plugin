package org.stackit.src;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class GiveManager {
	protected static HashMap<String, GiveType> array_types = new HashMap<String, GiveType>();
	
	/**
	 * Get all the types of things that can be given to a user.
	 * @return HashMap<String, GiveType> List of types
	 */
	public static HashMap<String, GiveType> getTypes() {
		return array_types;
	}
	
	/**
	 * Add a type of thing that can be given to a user.
	 * \!/ Does not work if the type is already supported \!/
	 * @param String Type
	 */
	public static void addType(String type, GiveType handler) {
		if(!isSupported(type)) {
			array_types.put(type.toLowerCase(), handler);
		}
	}
	
	/**
	 * Get the class manager of a type.
	 * @param String Type
	 * @return PageType Class manager
	 */
	private static GiveType getType(String type) {
		return getTypes().get(type);
	}
	
	/**
	 * Check if a type is supported.
	 * @String String Type
	 * @return Boolean Supported
	 */
	public static Boolean isSupported(String type) {
		if(getTypes().containsKey(type.toLowerCase())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Initiate the give manager.
	 */
	public static void init() {
		// Add the minimal supported types
		addType("item", new GiveItem());
	}
	
	/**
	 * Execute the handler of a type.
	 * @param String Type
	 * @param HashMap<String, String> Arguments
	 * @param HashMap<String, Object> Purchase
	 * @param String Player
	 */
	public static String execute(String type, HashMap<String, String> args, HashMap<String, Object> purchase, String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		
		if(player == null) {
			player = Bukkit.getPlayer(UUID.fromString(playerName));
			
			if(player == null) {
				return "user_not_connected";
			}
		}
		
		String answer = getType(type).handler(args, purchase, player);
		
		if(answer == "success") {
			String playerLanguage = Language.getPlayerLanguage(player);
			
			player.sendMessage(ChatColor.DARK_AQUA + "╔" + StringUtils.center(ChatColor.BOLD + "|[ " + ChatColor.AQUA + Language.get(playerLanguage, "congrats") + ChatColor.DARK_AQUA + ChatColor.BOLD + " ]|" + ChatColor.DARK_AQUA, 54, '═'));
			player.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.AQUA + Language.replace(Language.get(playerLanguage, "received_item"), "PURCHASE_TYPE", purchase.get("type").toString()));
			
			if(PurchaseManager.isAGift(purchase.get("id").toString())) {
				player.sendMessage(ChatColor.DARK_AQUA + "║ ");
				player.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.AQUA + Language.get(playerLanguage, "received_item_gift"));
				player.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.AQUA + Language.replace(Language.get(playerLanguage, "received_item_by"), "SENDER", purchase.get("buyer").toString()));
			}
			
			if(PurchaseManager.hasCustomMessage(purchase.get("id").toString())) {
				player.sendMessage(ChatColor.DARK_AQUA + "║ ");
				player.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.AQUA + Language.replace(Language.get(playerLanguage, "received_item_custom_message"), "PURCHASE_TYPE", purchase.get("type").toString()));
				player.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.AQUA + PurchaseManager.getCustomMessage(purchase.get("id").toString()));
			}
			
			player.sendMessage(ChatColor.DARK_AQUA + "╚" + StringUtils.center("", 34, '═'));
		}
		
		return answer;
	}

}
