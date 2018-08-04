package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Event;
import io.noctin.events.EventProxy;
import io.noctin.events.TriggerCallback;
import io.noctin.http.HttpEvent;
import io.noctin.http.HttpGetEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;

public class JsonRequest extends EventProxy {

    @Override
    public boolean forward(Event event) {

        HttpEvent e = (HttpEvent) event;

        e.response.type(ContentType.JSON_CONTENT_TYPE);

        try {
            new JsonConfiguration(e.request.body());
        } catch (Exception ex){

            JsonHeaders headers = new JsonHeaders();
            headers.status(HTTPStatus.BAD_REQUEST);
            headers.message("Malformed body (attempted JSON)");

            e.render(new RestEngine(headers).render());

            return false;
        }

        return true;
    }
}
