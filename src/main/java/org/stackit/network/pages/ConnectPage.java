package org.stackit.network.pages;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class ConnectPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
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

                    getContent().put("token", token);
                    getContent().put("expire", Long.toString(expireTimeStamp));

                    success("Token successfully generated");
                    Logger.userLoggedIn(user, "login");

                } else {
                    response.status(401);
                    error("Invalid password");
                    Logger.userTriedToLog(user, "login", "Invalid password");
                }
            } else {
                response.status(401);
                error("Invalid username");
                Logger.userTriedToLog(user, "login", "Invalid username");
            }
        } else {
            response.status(400);
            error("Bad request");
        }
    }
}
