package org.stackit.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemManager {
	
	/**
	 * Add enchants to the meta of an item.
	 * @param HashMap<String, Object> Item meta
	 * @param String Enchants
	 * @return ItemMeta Item meta
	 */
	public static ItemMeta addEnchants(ItemMeta meta, HashMap<String, Object> PurchaseArgs) {
		String[] enchantsTable = PurchaseArgs.get("enchants").toString().split(",");
		
		for(Integer i = 0 ; i < enchantsTable.length ; i++) {
			String[] encPower = enchantsTable[i].split(":");
			
			if(Enchantment.getByName(encPower[0].toUpperCase()) != null) {
				meta.addEnchant(Enchantment.getByName(encPower[0].toUpperCase()), Integer.parseInt(encPower[1]), true);
			}
		}
		
		return meta;
	}
	
	/**
	 * Set the display lore of an item.
	 * @param ItemMeta meta
	 * @param String Text
	 * @return ItemMeta meta
	 */
	public static ItemMeta setLore(ItemMeta meta, HashMap<String, Object> PurchaseArgs) {
		ArrayList<String> lores = new ArrayList<String>();
		String[] displayLore = PurchaseArgs.get("lore").toString().split("`;");
		
		for(Integer i = 0 ; i < displayLore.length ; i++) {
			lores.add(ChatColor.translateAlternateColorCodes('%', displayLore[i]));
		}
		
		meta.setLore(lores);
		
		return meta;
	}

}
