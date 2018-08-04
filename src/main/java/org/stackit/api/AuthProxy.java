package org.stackit.api;

import io.noctin.events.Event;
import io.noctin.events.EventProxy;
import io.noctin.http.HttpEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;

public class AuthProxy extends EventProxy {
    @Override
    public boolean forward(Event event) {

        HttpEvent e = (HttpEvent) event;

        e.response.type(ContentType.JSON_CONTENT_TYPE);

        if (e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME) == null){

            JsonHeaders headers = new JsonHeaders();
            headers.status(HTTPStatus.UNAUTHORIZED);

            e.render(new RestEngine(headers).render());
            return false;
        }

        return true;
    }
}
