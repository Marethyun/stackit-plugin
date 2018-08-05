package org.stackit;

import io.noctin.configuration.ErroneousConfigException;
import io.noctin.configuration.YamlConfiguration;
import io.noctin.events.EntityHandler;
import io.noctin.http.HttpHandler;
import io.noctin.http.HttpServer;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.api.*;
import spark.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;

public final class StackIt extends JavaPlugin {

    private final StackItLogger logger = new StackItLogger(getServer().getConsoleSender());

    public static final String CONFIGURATION_FILE = "StackIt.yml";

    public final PluginDescriptionFile description = getDescription();
    private static StackIt instance;
    private HttpServer server;
    private HttpHandler handler;

    public YamlConfiguration configuration;

    private StackItCommand command = new StackItCommand(this);
    private Bundler bundler;

    @Override
    public void onLoad() {
        try {

            instance = this;

            File configFile = this.resolveFile(CONFIGURATION_FILE);

            String yaml = FileUtils.readFileToString(configFile, Charset.defaultCharset());

            this.configuration = new YamlConfiguration(yaml);

            if (!this.configuration.areSet(ConfigNodes.toNodeArray()))
                throw new StackItException("Missing nodes in the configuration file");

            int port = this.configuration.getInt(ConfigNodes.API_PORT.getNode());

            if (portInUse(port)) throw new StackItException(String.format("The port %s is already in use", port));


            // Initialize the spark instance
            Service service = Service.ignite();
            service.port(port);

            // SSL/TLS Encryption
            if (this.configuration.getBoolean(ConfigNodes.SSL_ENABLED.getNode())) {
                String keyStorePath = this.configuration.getString(ConfigNodes.SSL_KEYSTORE_PATH.getNode());
                String trustStorePath = this.configuration.getString(ConfigNodes.SSL_KEYSTORE_PATH.getNode());

                String keyStorePass = this.configuration.getString(ConfigNodes.SSL_KEYSTORE_PASSPHRASE.getNode());
                String trustStorePass = this.configuration.getString(ConfigNodes.SSL_TRUST_STORE_PASSPHRASE.getNode());

                if (keyStorePath == null) {
                    throw new ErroneousConfigException("Keystore path isn't filled in configuration");
                }

                if (trustStorePath.isEmpty()) {
                    logger.warn("Trust store location not filled, clients will not be able to confirm certificates validity");
                    trustStorePath = null;
                    trustStorePass = null;
                }

                File keyStore = resolveFile(keyStorePath);

                service.secure(keyStore.getAbsolutePath(), keyStorePass, trustStorePath, trustStorePass);

            }

            // Initialize the HTTP stuff
            this.server = new HttpServer(service);
            this.handler = this.server.getEventHandler();

            this.bundler = new Bundler(this);
        } catch (StackItException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new StackItException(e);
        }
    }

    @Override
    public void onEnable() {

        this.getCommand("stackit").setExecutor(this.command);

        this.handler.attach(new RootListener(this));
        this.handler.attach(new AuthenticationListener(this));
        this.handler.attach(new RemoteCommandsListener(this));
        this.handler.attach(new PlayerBansListener(this));
        this.handler.attach(new IPBansListener(this));
        this.handler.attach(new WhitelistListener(this));
        this.handler.attach(new PlayersListListener(this));

        this.server.run();
    }

    @Override
    public void onDisable() {
        if (server != null) this.server.stop();
        this.server = null;
        this.handler = null;
        instance = null;
    }

    private static StackIt getInstance(){
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

    public LinkedList<Account> getAccounts(){
        ArrayList<String> array = configuration.getStringArray(ConfigNodes.API_ACCOUNTS.getNode());

        LinkedList<Account> accounts = new LinkedList<>();

        for (String s : array) {
            accounts.add(new Account(s));
        }

        return accounts;
    }

    /**
     * Resolve a file: if it doesn't exists in datafolder, just create it from the template in path
     * Returns the file content anyway
     * @param fileName The file name
     */
    private File resolveFile(String fileName) {
        File file = new File(this.getDataFolder(), fileName);

        if (!file.exists()){
            try (InputStream stream = getClass().getResourceAsStream('/' + fileName)){
                System.out.println(fileName);

                String content = "";

                int i;

                while ((i = stream.read()) != -1){
                    content += (char) i;
                }

                FileUtils.writeStringToFile(file, content, Charset.defaultCharset());
            } catch (Exception e){
                e.printStackTrace();
                throw new StackItException(e);
            }
        }

        return file;
    }

    public Bundler getBundler() {
        return bundler;
    }

    /**
     * Just a hook of the previous getter
     * @return The StackIt bundler
     */
    public static Bundler bundler(){
        return StackIt.getInstance().getBundler();
    }

    public StackItLogger logger() {
        return logger;
    }

    public EntityHandler getHandler() {
        return handler;
    }

    public StackItCommand getCommand() {
        return command;
    }
}
