package org.stackit.api;

import io.noctin.NoctinException;
import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpPostEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;
import org.stackit.Account;
import org.stackit.StackIt;

import java.util.LinkedList;
import java.util.UUID;

public class AuthenticationListener implements Listener {

    public static final String SESSION_ATTRIBUTE_NAME = "remote_id";

    @Trigger @EndPoint("/auth") @Before(BeforeAPI.class)
    public void authenticate(HttpPostEvent e){
        JsonHeaders headers = new JsonHeaders();

        if (e.request.session().attribute(SESSION_ATTRIBUTE_NAME) == null){

            try {
                JsonConfiguration configuration = new JsonConfiguration(e.request.body());

                if (configuration.areSet("username", "password")) {

                    String username = configuration.getString("username");
                    String password = configuration.getString("password");

                    Account account = new Account(username, password);

                    LinkedList<Account> accounts = StackIt.getInstance().getAccounts();

                    if (accounts.contains(account)){

                        UUID uuid = UUID.randomUUID();

                        e.request.session(true);
                        e.request.session().attribute(SESSION_ATTRIBUTE_NAME, uuid.toString());

                        StackIt.LOGGER.success(String.format("Remote with UUID '%s' (%s) successfully authenticated with account '%s'", uuid, e.request.ip(), account));

                        headers.message("Remote successfully authenticated, session created");
                    } else {
                        StackIt.LOGGER.warn(String.format("Remote with ip '%s' tried to authenticate with invalid account '%s'", e.request.ip(), account));
                        headers.status(HTTPStatus.UNAUTHORIZED);
                    }
                } else {
                    headers.status(HTTPStatus.BAD_REQUEST);
                }
            } catch (Exception exception){
                headers.message(exception.getMessage());
                headers.status(HTTPStatus.BAD_REQUEST);
            }
        } else {
            headers.status(HTTPStatus.FORBIDDEN);
            headers.message("Remote already authenticated");
        }

        e.render(new RestEngine(headers).render());
    }
}
