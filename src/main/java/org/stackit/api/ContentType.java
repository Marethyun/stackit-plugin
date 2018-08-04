package org.stackit.api;

import io.noctin.events.Event;
import io.noctin.events.TriggerCallback;
import io.noctin.http.HttpEvent;

public final class ContentType extends TriggerCallback {

    public static final String JSON_CONTENT_TYPE = "application/json";

    @Override
    public void run(Event event) {
        HttpEvent e = (HttpEvent) event;

        e.response.type(JSON_CONTENT_TYPE);
    }
}
