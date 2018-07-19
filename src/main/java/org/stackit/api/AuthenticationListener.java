package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Listener;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpPostEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;

public class AuthenticationListener implements Listener {

    public static final String SESSION_ATTRIBUTE_NAME = "remote_id";

    @Trigger @EndPoint("/auth")
    public void authenticate(HttpPostEvent e){
        JsonHeaders headers = new JsonHeaders();

        if (e.request.session().attribute(SESSION_ATTRIBUTE_NAME) == null){
            headers.message("Remote successfully authenticated, session created");
        } else {
            headers.status(HTTPStatus.FORBIDDEN);
            headers.message("Remote already authenticated");
        }

        e.render(new RestEngine(headers).render());
    }
}
