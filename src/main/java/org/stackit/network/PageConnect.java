package org.stackit.network;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PageConnect implements Page {

    /**
     * Handler for the page /login.
     */
    @Override
    public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer) {
	    String user = null, pass = null;
	
	    if (!StackItConfiguration.isInMaintenance()) {
	
	        // If _GET['user'] or _GET['password'] are present and doesn't equal to ""
	        if ((user = request.queryParams("user")) != null && (pass = request.queryParams("pass")) != null) {
	            Map<String, String> accounts = StackItConfiguration.getAccounts();
	
	            if (accounts.containsKey(user)) {
	                if (accounts.get("user").equals(pass)) {
	                    String token = UUID.randomUUID().toString();
	                    long expireTimeStamp = System.currentTimeMillis() + StackItConfiguration.getTokensExpiration();
	
	                    DatabaseManager.getTokens().add(System.currentTimeMillis(), token);
	
	                    answer.put("status", StatusType.SUCCESS);
	                    answer.put("token", token);
	                    answer.put("expire", Long.toString(expireTimeStamp));
	                    answer.put("message", "Token successfully generated");
	
	                    Logger.userLoggedIn(user, "login");
	                } else {
	                    answer.put("status", "error");
	                    answer.put("message", "Invalid password");
	
	                    Logger.userTriedToLog(user, "login", "Invalid password");
	                }
	            } else {
	                answer.put("status", StatusType.ERROR);
	                answer.put("message", "Invalid username");
	
	                Logger.userTriedToLog(user, "login", "Invalid username");
	            }
	        } else {
	           answer.put("status", StatusType.ERROR);
	           answer.put("message", "Bad request");
	        }
	    } else {
	        answer.put("status", StatusType.ERROR);
	        answer.put("message", "API is currently disabled");
	    }
	        
	    return answer;
    }
}
