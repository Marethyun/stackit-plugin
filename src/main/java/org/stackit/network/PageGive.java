package org.stackit.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.src.GiveManager;
import org.stackit.src.PurchaseManager;
import org.stackit.src.TokenManager;

import com.sun.net.httpserver.HttpExchange;

public class PageGive implements Page {

	@Override
	public HashMap<String, Object> handle(HttpExchange exchange, HashMap<String, Object> answer) {
		String token = null, player = null, auth = null, purchaseId = null, type = null;
		
		if(!StackItConfiguration.isInMaintenance()) {
			if((token = MainWebServer.GETO(exchange, "token")) != null && (purchaseId = MainWebServer.GETO(exchange, "pid")) != null) {
				if((auth = TokenManager.Auth(token)).equalsIgnoreCase("success")) {
					HashMap<String, Object> purchase = PurchaseManager.getPurchase(purchaseId);
					
					if(purchase.size() > 0) {
						type = purchase.get("type").toString();
						player = purchase.get("buy_for").toString();
						
						if(GiveManager.isSupported(type)) {
							Player onPlayer = null;
	
							if(player.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
								onPlayer = Bukkit.getPlayer(UUID.fromString(player));
							} else {
								onPlayer = Bukkit.getPlayer(player);
							}
							
							if(onPlayer == null) {
								if(!PurchaseManager.isPurchaseInWaitingList(purchaseId)) {
									PurchaseManager.addPurchaseToWaitingList(player, purchaseId);
								}
								
								answer.put("error", "user_not_connected");
								answer.put("errorMessage", "The object will be given to the player when he will be connected.");
								answer.put("success", false);
							} else {
								String result = GiveManager.execute(type, MainWebServer.GETALL(exchange), purchase, player);
								
								if(result.equalsIgnoreCase("success")) {
									answer.put("player", player);
									answer.put("purchaseId", purchaseId);
									answer.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
									answer.put("time", new SimpleDateFormat("HH:mm:ss").format(new Date()));
									answer.put("success", true);
									
									DatabaseManager.getActiveDatabase().RemovePurchase(purchaseId);
								} else {
									answer.put("error", result);
									answer.put("success", false);
								}
							}
						} else {
							answer.put("error", "invalid_type");
							answer.put("success", false);
						}
					} else {
						answer.put("error", "invalid_purchase");
						answer.put("success", false);
					}
				} else {
					answer.put("error", auth);
					answer.put("success", false);
				}
			} else {
				answer.put("error", "not_enough_arguments");
				answer.put("success", false);
			}
		} else {
			answer.put("error", StackItConfiguration.getAPIMaintenanceError());
			answer.put("success", false);
		}
		
		return answer;
	}

}
