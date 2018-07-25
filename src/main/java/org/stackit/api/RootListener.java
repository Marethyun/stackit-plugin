package org.stackit.api;

import io.noctin.configuration.Configuration;
import io.noctin.configuration.JsonConfiguration;
import io.noctin.events.After;
import io.noctin.events.Before;
import io.noctin.events.Listener;
import io.noctin.events.Trigger;
import io.noctin.http.EndPoint;
import io.noctin.http.HttpGetEvent;
import io.noctin.network.http.server.renderer.RestEngine;
import org.bukkit.plugin.PluginDescriptionFile;
import org.stackit.StackIt;

public class RootListener implements Listener {
    @Trigger @EndPoint("/") @Before(BeforeAPI.class)
    public void root(HttpGetEvent e){

        JsonConfiguration configuration = JsonConfiguration.createBlank();

        PluginDescriptionFile description = StackIt.getInstance().description;

        configuration.set("stackit-version", description.getVersion());

        e.render(new RestEngine(configuration).render());
    }
}
