package org.stackit.api;

import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Proxy;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpGetEvent;
import io.noctin.network.http.server.renderer.RestEngine;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.StackItContainer;

import java.util.LinkedList;

public final class PlayersListListener extends StackItContainer implements Listener{

    private Server server = this.pluginInstance.getServer();

    public PlayersListListener(StackIt pluginInstance) {
        super(pluginInstance);
    }

    /**
     * Returns online players UUIDs
     * @param e
     */
    @Trigger
    @EndPoint("/players") @Proxy(AuthProxy.class) @Before(ContentType.class)
    public void get(HttpGetEvent e){
        JsonConfiguration configuration = JsonConfiguration.createBlank();

        LinkedList<String> uniqueIds = new LinkedList<>();

        for (Player player : server.getOnlinePlayers()) {
            uniqueIds.add(player.getUniqueId().toString());
        }

        configuration.set("count", uniqueIds.size());
        configuration.set("online", uniqueIds);

        e.render(new RestEngine(configuration).render());
    }
}
