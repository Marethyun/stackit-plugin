package org.stackit.network.pages;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class ConnectPage extends Page {

    /**
     * Handler for the page /connect.
     */
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


                    getContent().get().put("token", token);
                    getContent().get().put("expire", Long.toString(expireTimeStamp));

                    setAPIState(StatusType.SUCCESS);
                    setMessage("Token successfully generated");
                    Logger.userLoggedIn(user, "login");

                } else {
                    setAPIState(StatusType.ERROR);
                    addErrorMessage("Invalid password");
                    Logger.userTriedToLog(user, "login", "Invalid password");
                }
            } else {
                setAPIState(StatusType.ERROR);
                addErrorMessage("Invalid username");
                Logger.userTriedToLog(user, "login", "Invalid username");
            }
        } else {
            setAPIState(StatusType.ERROR);
            addErrorMessage("Bad request");
        }
    }
}
