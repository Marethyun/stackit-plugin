package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.*;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public final class WhitelistListener implements Listener {

    private static Server server = StackIt.getInstance().getServer();

    /**
     * Returns whitelisted players UUIDs
     * @param e GET /whitelist
     */
    @Trigger
    @EndPoint("/whitelist") @Proxy(AuthProxy.class) @Before(ContentType.class)
    public void get(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        LinkedList<String> uniqueIds = new LinkedList<>();

        for (OfflinePlayer offlinePlayer : server.getWhitelistedPlayers()) {
            uniqueIds.add(offlinePlayer.getUniqueId().toString());
        }

        configuration.set("enabled", Bukkit.hasWhitelist());
        configuration.set("count", uniqueIds.size());
        configuration.set("whitelisted", uniqueIds);

        e.render(new RestEngine(configuration).render());
    }

    /**
     * Adds a player to the whitelist
     * @param e PUT /whitelist
     */
    @SuppressWarnings("deprecation")
    @Trigger
    @EndPoint("/whitelist") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void put(HttpPutEvent e){
        JsonConfiguration body = new JsonConfiguration(e.request.body());
        JsonHeaders headers = new JsonHeaders();

        if (body.isSet("player")){
            String playerName = body.getString("player");

            Bukkit.getOfflinePlayer(playerName).setWhitelisted(true);

            StackIt.LOGGER.success(String.format("Remote with UUID '%s' successfully whitelisted player %s", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), playerName));
        } else {
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(headers).render());
    }

    /**
     * Removes a player to the whitelist
     * @param e DELETE /whitelist
     */
    @SuppressWarnings("deprecation")
    @Trigger
    @EndPoint("/whitelist") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void delete(HttpDeleteEvent e){
        JsonConfiguration body = new JsonConfiguration(e.request.body());
        JsonHeaders headers = new JsonHeaders();

        if (body.isSet("player")){
            String playerName = body.getString("player");

            Bukkit.getOfflinePlayer(playerName).setWhitelisted(false);

            StackIt.LOGGER.success(String.format("Remote with UUID '%s' successfully un-whitelisted player %s", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), playerName));
        } else {
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(headers).render());
    }

    @Trigger
    @EndPoint("/whitelist") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void patch(HttpPatchEvent e){
        JsonConfiguration body = new JsonConfiguration(e.request.body());
        JsonHeaders headers = new JsonHeaders();

        if (body.isSet("enable")){
            boolean enabled = body.getBoolean("enable");

            Bukkit.setWhitelist(enabled);

            StackIt.LOGGER.success(String.format("Remote with UUID '%s' successfully %s the whitelist", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), enabled ? "enabled" : "disabled"));
        } else {
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(headers).render());
    }
}
