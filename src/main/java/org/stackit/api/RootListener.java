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
import org.bukkit.plugin.PluginDescriptionFile;
import org.stackit.StackIt;

public class RootListener implements Listener {

    private static Server server = StackIt.getInstance().getServer();

    @Trigger @EndPoint("/") @Before(ContentType.class)
    public void root(HttpGetEvent e){

        JsonConfiguration configuration = JsonConfiguration.createBlank();

        PluginDescriptionFile description = StackIt.getInstance().description;

        configuration.set("stackit-version", description.getVersion());

        e.render(new RestEngine(configuration).render());
    }

    /**
     * Returns basic server info
     * @param e GET /info
     */
    @Trigger @EndPoint("/info") @Proxy(AuthProxy.class) @Before(ContentType.class)
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

}
