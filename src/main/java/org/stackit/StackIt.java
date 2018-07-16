package org.stackit;

import io.noctin.http.HttpHandler;
import io.noctin.http.HttpServer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.api.RootListener;
import spark.Service;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class StackIt extends JavaPlugin {

    public final PluginDescriptionFile description = getDescription();
    private static StackIt instance;
    private HttpServer server;
    private HttpHandler handler;

    // TODO: REPLACE
    private final int apiPort = 9600;

    @Override
    public void onLoad() {
        instance = this;

        if (portInUse(apiPort)){
            getServer().getPluginManager().disablePlugin(this);
        } else {

            // TODO: LOAD PLUGIN CONFIGURATION (FROM YAML)

            Service service = Service.ignite();
            service.port(apiPort);

            this.server = new HttpServer(service);
            this.handler = this.server.getEventHandler();
        }
    }

    @Override
    public void onEnable() {
        this.handler.attach(new RootListener());

        this.server.run();
    }

    @Override
    public void onDisable() {
        this.server.stop();
    }

    public static StackIt getInstance(){
        return instance;
    }

    private boolean portInUse(int port){
        // Assume no connection is possible
        boolean result = false;

        try {
            (new Socket("127.0.0.1", port)).close();
            // Connection is possible, port is in use
            result = true;
        } catch (IOException ignored) {}

        return result;
    }
}
