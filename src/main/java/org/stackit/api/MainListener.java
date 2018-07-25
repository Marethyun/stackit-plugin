package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpGetEvent;
import io.noctin.network.http.server.renderer.RestEngine;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;

import java.util.*;

public class MainListener implements Listener {

    private static Server server = StackIt.getInstance().getServer();

    /**
     * Returns basic server info
     * @param e GET /info
     */
    @Trigger @EndPoint("/info") @Proxy(AuthProxy.class) @Before(BeforeAPI.class)
    public void serverInfo(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        configuration.set("modt", server.getMotd());
        configuration.set("name", server.getName());
        configuration.set("version", server.getBukkitVersion());
        configuration.set("online_players", server.getOnlinePlayers().size());
        configuration.set("max_players", server.getMaxPlayers());
        configuration.set("banned_players", server.getBannedPlayers().size());
        configuration.set("fly", server.getAllowFlight());
        configuration.set("nether", server.getAllowNether());
        configuration.set("end", server.getAllowEnd());
        configuration.set("structures", server.getGenerateStructures());
        configuration.set("plugins", server.getPluginManager().getPlugins().length);

        e.render(new RestEngine(configuration).render());
    }

    /**
     * Returns online players UUIDs
     * @param e
     */
    @Trigger @EndPoint("/online") @Proxy(AuthProxy.class) @Before(BeforeAPI.class)
    public void onlinePlayers(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        LinkedList<UUID> uniqueIds = new LinkedList<>();

        for (Player player : server.getOnlinePlayers()) {
            uniqueIds.add(player.getUniqueId());
        }

        configuration.set("count", uniqueIds.size());
        configuration.set("online", uniqueIds);

        e.render(new RestEngine(configuration).render());
    }

    /**
     * Returns banned players UUIDs
     * @param e GET /banned
     */
    @Trigger @EndPoint("/banned") @Proxy(AuthProxy.class) @Before(BeforeAPI.class)
    public void bannedPlayers(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        LinkedList<UUID> uniqueIds = new LinkedList<>();

        for (OfflinePlayer offlinePlayer : server.getBannedPlayers()) {
            uniqueIds.add(offlinePlayer.getUniqueId());
        }

        configuration.set("count", uniqueIds.size());
        configuration.set("banned", uniqueIds);

        e.render(new RestEngine(configuration).render());
    }
}
