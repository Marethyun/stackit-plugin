package org.stackit.network.pages;

import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class ConnectPage extends Page {

    @Override
    public void handle() {
	    String pass, user;
	    Request request = getRequest();
	    Response response = getResponse();
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

                    status(StatusMessage.SUCCESS, StatusType.SUCCESS);

                } else {
                    status(StatusMessage.INVALID_CREDENTIALS, StatusType.UNAUTHORIZED);
                }
            } else {
                status(StatusMessage.INVALID_CREDENTIALS, StatusType.UNAUTHORIZED);
            }
        } else {
            status(StatusMessage.BAD_REQUEST, StatusType.BAD_REQUEST);
        }
    }
}
