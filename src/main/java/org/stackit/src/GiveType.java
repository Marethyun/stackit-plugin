package org.stackit.src;

import java.util.HashMap;

import org.bukkit.entity.Player;

public interface GiveType {
	
	/**
	 * Manage the give-type of a request on /give.
	 * @param String _GET arguments
	 * @return String Success (or error name)
	 */
	public String handler(HashMap<String, String> args, HashMap<String, Object> purchase, Player player);

}
