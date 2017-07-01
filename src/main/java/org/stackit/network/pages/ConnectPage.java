package org.stackit.network.pages;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectPage implements Page {

    /**
     * Handler for the page /connect.
     */
    @Override
    public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer, HashMap<String, Object> responseContent) throws Exception {
	    String pass, user;
        if (request.queryParams().contains("pass") && request.queryParams().contains("user")) {
            pass = request.queryParams("pass");
            user = request.queryParams("user");
            Map<String, String> accounts = StackItConfiguration.getAccounts();
            if (accounts.containsKey(user)) {
                if (accounts.get(user).equals(pass)) {
                    String token = UUID.randomUUID().toString();
                    long expireTimeStamp = System.currentTimeMillis() + StackItConfiguration.getTokensExpiration();
                    DatabaseManager.getTokens().add(System.currentTimeMillis(), token);

                    answer.put("status", StatusType.SUCCESS);
                    responseContent.put("token", token);
                    responseContent.put("expire", Long.toString(expireTimeStamp));
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
	        
	    return answer;
    }
}
