package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpDeleteEvent;
import io.noctin.http.HttpGetEvent;
import io.noctin.http.HttpPutEvent;
import io.noctin.network.http.server.HTTPStatus;
import io.noctin.network.http.server.renderer.JsonHeaders;
import io.noctin.network.http.server.renderer.RestEngine;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.stackit.StackIt;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

public class IPBansListener implements Listener {
    @Trigger
    @EndPoint("/bannedips") @Proxy(AuthProxy.class) @Before(ContentType.class)
    public void get(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        LinkedList<JsonConfiguration> bans = new LinkedList<>();

        Set<BanEntry> banEntries = Bukkit.getBanList(BanList.Type.IP).getBanEntries();

        for (BanEntry entry : banEntries) {
            JsonConfiguration ban = JsonConfiguration.createBlank();

            ban.set("ip", entry.getTarget());
            ban.set("source", entry.getSource());
            ban.set("reason", entry.getReason());
            ban.set("created", entry.getCreated().getTime());
            ban.set("expires", entry.getExpiration() == null ? -1 : entry.getExpiration().getTime());

            bans.add(ban);
        }

        configuration.set("count", bans.size());
        configuration.set("banned", bans);

        e.render(new RestEngine(configuration).render());
    }

    /**
     * Ban a player
     * @param e PUT /bannedplayers
     */
    @Trigger
    @EndPoint("/bannedips") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void put(HttpPutEvent e){

        JsonConfiguration configuration = new JsonConfiguration(e.request.body());
        JsonHeaders headers = new JsonHeaders();

        if (configuration.areSet("ip", "reason")){
            String ipAddress = configuration.getString("ip");
            String reason = configuration.getString("reason");
            Date expires = null;
            String source = Bukkit.getConsoleSender().getName();

            if (configuration.isSet("expires")){
                if (configuration.getLong("expires") == null){
                    expires = null;
                } else {
                    expires = new Date(configuration.getLong("expires"));
                }
            }

            if (configuration.isSet("source")){
                source = configuration.getString("source");
            }

            Bukkit.getBanList(BanList.Type.IP).addBan(ipAddress, reason, expires, source);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getAddress().getAddress().getHostAddress().equals(ipAddress)) {
                    player.kickPlayer("You have been IP banned.");
                }
            }

            StackIt.LOGGER.success(String.format("Remote with UUID '%s' Successfully banned ip address '%s'", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), ipAddress));

        } else {
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(headers).render());
    }

    @Trigger
    @EndPoint("/bannedips") @Proxy({AuthProxy.class, JsonRequest.class}) @Before(ContentType.class)
    public void delete(HttpDeleteEvent e){
        JsonConfiguration body = new JsonConfiguration(e.request.body());
        JsonHeaders headers = new JsonHeaders();

        if (body.isSet("ip")){
            String ipAddress = body.getString("ip");

            Bukkit.getBanList(BanList.Type.IP).pardon(ipAddress);

            StackIt.LOGGER.success(String.format("Remote with UUID '%s' Successfully unbanned ip address '%s'", e.request.session().attribute(AuthenticationListener.SESSION_ATTRIBUTE_NAME), ipAddress));

        } else {
            headers.status(HTTPStatus.BAD_REQUEST);
        }

        e.render(new RestEngine(headers).render());
    }
}
