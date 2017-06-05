package org.stackit.network;

import java.util.HashMap;

import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.src.Logger;

import com.sun.net.httpserver.HttpExchange;

public class PageConnect implements Page {

	/**
	 *	Handler for the page /login.
	 */
	@Override
	public HashMap<String, java.lang.Object> handle(HttpExchange exchange, HashMap<String, Object> answer) {
		exchange = MainWebServer.setHeaders(exchange);
		String user = null, pass = null;
		
		if(!StackItConfiguration.isInMaintenance()) {
			
			// If _GET['user'] or _GET['password'] are present and doesn't equal to ""
			if((user = MainWebServer.GETO(exchange, "user")) != null && (pass = MainWebServer.GETO(exchange, "pass")) != null) {

				String result = DatabaseManager.getActiveDatabase().AuthUser(user, pass);
				
				if(result.equalsIgnoreCase("success")) {
					String token = DatabaseManager.getActiveDatabase().GenerateToken(user);
					
					answer.put("token", token);
					answer.put("expire", DatabaseManager.getActiveDatabase().GetTokenExpiration(token));
					answer.put("success", true);
					
					Logger.pushUserLoggedInLog(user, "/login", exchange.getRemoteAddress().toString());
				} else {
					answer.put("error", result);
					answer.put("success", false);
					
					Logger.pushUserTriedToLogInLog(user, "/login", result, exchange.getRemoteAddress().toString());
				}
			} else {
				answer.put("error", "not_enough_argument");
				answer.put("success", false);
	
				Logger.pushUserTriedToLogInLog(user, "/login", "not_enough_arguments", exchange.getRemoteAddress().toString());
			}
		} else {
			answer.put("error", StackItConfiguration.getAPIMaintenanceError());
			answer.put("success", false);
		}
		
		return answer;
	}
}
