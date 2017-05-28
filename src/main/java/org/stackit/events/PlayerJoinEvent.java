package org.stackit.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.stackit.dtb.Database;
import org.stackit.src.GiveManager;
import org.stackit.src.PurchaseManager;
import org.stackit.src.StackIt;

public class PlayerJoinEvent implements Listener {
	
	@EventHandler
	public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		StackIt.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(StackIt.getPlugin(), new Runnable() {
			public void run() {
				// If the user has purchase in the waiting list (User name or UUID)
				if(PurchaseManager.hasPurchaseInWaitingList(player.getUniqueId().toString()) || PurchaseManager.hasPurchaseInWaitingList(player.getName())) {
					HashMap<Integer, String> purchases = PurchaseManager.getPurchasesInWaitingList(player.getName(), player.getUniqueId().toString());
					
					for(Integer i = 0 ; i < purchases.size() ; i++) {
						HashMap<String, Object> purchase = PurchaseManager.getPurchase(purchases.get(i));
						
						String type = purchase.get("type").toString();
						String playerTarget = purchase.get("buy_for").toString();
						HashMap<String, String> args = new HashMap<String, String>();
						
						if(GiveManager.isSupported(type)) {
							String result = GiveManager.execute(type, args, purchase, playerTarget);
							
							if(result == "success") {
								Database.getActiveDatabase().RemovePurchase(purchases.get(i));
							}
						}
					}
				}
			}
		}, 20L);
	}

}
