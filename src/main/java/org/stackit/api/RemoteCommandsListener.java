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
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.stackit.StackIt;

public final class RemoteCommandsListener implements Listener {

    private static Server server = StackIt.getInstance().getServer();

    @Trigger @EndPoint("/command") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void execute(HttpPostEvent e){

        JsonHeaders headers = new JsonHeaders();
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        try {
            JsonConfiguration body = new JsonConfiguration(e.request.body());

            if (body.isSet("command")){
                String command = body.getString("command");

                boolean success = server.dispatchCommand(Bukkit.getConsoleSender(), command);

                configuration.set("success", success);

                StackIt.LOGGER.warn(String.format("Remote with UUID '%s' issued command '%s' (success: %s)", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), command, success));
            } else {
                headers.status(HTTPStatus.BAD_REQUEST);
            }
        } catch (Exception exception){
            headers.message(exception.getClass().getName() + exception.getMessage());
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(configuration, headers).render());
    }
}