package org.stackit.network;

import java.util.HashMap;

import org.stackit.config.StackItConfiguration;
import org.stackit.src.Logger;
import org.stackit.src.PermissionManager;
import org.stackit.src.TokenManager;

import com.sun.net.httpserver.HttpExchange;

public class PageDebug implements Page {

	/**
	 *	Handler for the page /debug.
	 */
	public HashMap<String, Object> handle(HttpExchange exchange, HashMap<String, Object> answer) {
		exchange = MainWebServer.setHeaders(exchange);
		String token = null, auth = null;
		
		if(!StackItConfiguration.isInMaintenance()) {
			if((token = MainWebServer.GETO(exchange, "token")) != null) {
				if((auth = TokenManager.Auth(token)).toString().equalsIgnoreCase("success")) {
					if(PermissionManager.tokenHasEnoughPermissionsForContext("/debug", token)) {
						answer.put("answer", "Hello world!");
						answer.put("token", token);
						answer.put("success", true);
					} else {
						answer.put("error", "not_enough_permissions");
						answer.put("token", token);
						answer.put("success", false);
					}
				} else {
					answer.put("error", auth);
					answer.put("token", token);
					answer.put("success", false);
				}
			} else {
				answer.put("error", "missing_token");
				answer.put("success", false);
			}
			
			Logger.pushContextLog("/debug", TokenManager.getTokenUser(token), exchange.getRemoteAddress().toString());
		} else {
			answer.put("error", StackItConfiguration.getAPIMaintenanceError());
			answer.put("success", false);
		}
		
		return answer;
    }
}
