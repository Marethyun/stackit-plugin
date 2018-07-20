package org.stackit;

import io.noctin.configuration.YamlConfiguration;
import io.noctin.http.HttpHandler;
import io.noctin.http.HttpServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.api.AuthenticationListener;
import org.stackit.api.RootListener;
import org.stackit.api.StackItException;
import spark.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StackIt extends JavaPlugin {

    public final PluginDescriptionFile description = getDescription();
    private static StackIt instance;
    private HttpServer server;
    private HttpHandler handler;

    public static final String CONFIGURATION_FILE = "StackIt.yml";

    public YamlConfiguration configuration;

    private boolean disabling = false;

    @Override
    public void onLoad() {
        try {
            instance = this;
            Logger.getLogger("spark").setLevel(Level.ALL);

            File config = new File(this.getDataFolder() + File.separator + CONFIGURATION_FILE);

            String yaml = "";

            if (!config.exists()){
                // Read the configuration file from classPath
                InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIGURATION_FILE);

                int i;
                while ((i = in.read()) != -1){
                    yaml += (char) i;
                }

            } else {
                yaml = FileUtils.readFileToString(config, Charset.defaultCharset());
            }

            this.configuration = new YamlConfiguration(yaml);

            if (!this.configuration.areSet(ConfigNodes.toNodeArray())) throw new StackItException("Missing nodes in the configuration file");

            int port = this.configuration.getInt(ConfigNodes.API_PORT.getNode());

            if (portInUse(port)) throw new StackItException(String.format("The port %s is already in use", port));

            // Initialize the spark instance
            Service service = Service.ignite();
            service.port(port);

            // Initialize the HTTP stuff
            this.server = new HttpServer(service);
            this.handler = this.server.getEventHandler();
            System.out.println("");
        } catch (Exception e){
            e.printStackTrace();
            throw new StackItException(e);
        }
    }

    @Override
    public void onEnable() {
        this.handler.attach(new RootListener());
        this.handler.attach(new AuthenticationListener());

        this.server.run();
    }

    @Override
    public void onDisable() {
        if (server != null) this.server.stop();
        this.server = null;
        this.handler = null;
        instance = null;
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

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
