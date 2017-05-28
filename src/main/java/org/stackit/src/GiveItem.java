package org.stackit.src;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class GiveItem implements GiveType {

	@SuppressWarnings("deprecation")
	@Override
	public String handler(HashMap<String, String> args, HashMap<String, Object> purchase, Player player) {
		ItemStack item = null;
		
		if(purchase != null) {
			HashMap<String, Object> PurchaseArgs = PurchaseManager.getPurchaseArguments(purchase.get("id").toString());
			item = new ItemStack(Integer.parseInt(purchase.get("buy").toString()), 0, (short) Integer.parseInt(purchase.get("buy_sid").toString()));
			ItemMeta meta = item.getItemMeta();
			
			item.setAmount(Integer.parseInt(purchase.get("quantity").toString()));
			
			if(PurchaseArgs.containsKey("name")) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('%', PurchaseArgs.get("name").toString()));
			}
			
			if(PurchaseArgs.containsKey("lore")) {
				meta = ItemManager.setLore(meta, PurchaseArgs);
			}
			
			if(PurchaseArgs.containsKey("enchants")) {
				meta = ItemManager.addEnchants(meta, PurchaseArgs);
			}
			
			item.setItemMeta(meta);
			
			player.getInventory().addItem(item);
		    
			return "success";
		} else {
			return "invalid_purchase";
		}
	}
} 