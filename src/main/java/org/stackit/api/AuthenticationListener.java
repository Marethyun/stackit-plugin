package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpPostEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;
import org.stackit.*;

import java.util.LinkedList;
import java.util.UUID;

public final class AuthenticationListener extends StackItContainer implements Listener {

    public static final String SESSION_ATTRIBUTE_NAME = "remote_id";

    private final StackItLogger LOGGER = pluginInstance.logger();

    public AuthenticationListener(StackIt pluginInstance) {
        super(pluginInstance);
    }

    @Trigger
    @EndPoint("/auth") @Proxy(JsonRequest.class) @Before(ContentType.class)
    public void authenticate(HttpPostEvent e){
        JsonHeaders headers = new JsonHeaders();

        if (e.request.session().attribute(SESSION_ATTRIBUTE_NAME) == null){

            try {
                JsonConfiguration body = new JsonConfiguration(e.request.body());

                if (body.areSet("username", "password")) {

                    String username = body.getString("username");
                    String password = body.getString("password");

                    Account account = new Account(username, password);

                    LinkedList<Account> accounts = pluginInstance.getAccounts();

                    if (accounts.contains(account)){

                        UUID uuid = UUID.randomUUID();

                        e.request.session(true);

                        int sessionExpiration = pluginInstance.configuration.getInt(ConfigNodes.API_SESSION_EXPIRE.getNode());

                        e.request.session().maxInactiveInterval(sessionExpiration);
                        e.request.session().attribute(SESSION_ATTRIBUTE_NAME, uuid.toString());

                        LOGGER.success(String.format("Remote with UUID '%s' (%s) successfully authenticated with account '%s'", uuid, e.request.ip(), account));

                        headers.message("Remote successfully authenticated, session created");
                    } else {
                        LOGGER.warn(String.format("Remote with ip '%s' tried to authenticate with invalid account '%s'", e.request.ip(), account));
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
