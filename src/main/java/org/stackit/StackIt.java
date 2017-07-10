package org.stackit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.commands.MainCommand;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.Token;
import org.stackit.events.PlayerJoin;
import org.stackit.network.TokenManager;
import org.stackit.network.WebHandler;
import org.stackit.network.WebServer;
import org.stackit.network.pages.*;

import java.util.List;
import java.util.UUID;

/**
 * StackIt: A powerful shop for Minecraft 1.7-1.11
 * 
 * @author shamelin
 */
public class StackIt extends JavaPlugin {
	static Plugin plugin = null;
	static DatabaseManager dbManager;
	static int task;

    /**
	 * Initiate the plugin. Do not modify unless you are adding functionalities.
	 * 
	 * @author shamelin
	 */
	public void onEnable() {
        try {
            plugin = this;

            // Initiate plugin's logger
            Logger.init();

            // Load the main configuration file and check if the plugin is enabled
            StackItConfiguration.init();
            StackItConfiguration.checksEnabled();

            // Start database managing
            DatabaseManager.init();

            // Start the API
            WebServer.init();

            // Init routes
            WebHandler.addHandler("/connect", ConnectPage.class);
            WebHandler.addHandler("/gpi", GeneralPurposeInfoPage.class);
            WebHandler.addHandler("/players", PlayersPage.class);
            WebHandler.addHandler("/banlist", BanlistPage.class);
            WebHandler.addHandler("/whitelist", WhitelistPage.class);
            WebHandler.addHandler("/debug", DebugPage.class);

            // Register the events
            PluginManager pm = Bukkit.getPluginManager();

            pm.registerEvents(new PlayerJoin(), this);

            Logger.info("Plugin successfully loaded");

            // Add command executor
            getCommand("stackit").setExecutor(new MainCommand());

            // Start scheduler
            startTokensScheduler();
            Logger.info("Plugin successfully started");

        } catch (Exception e){
            e.printStackTrace();
            StackIt.disable();
        }

	}
	
	/**
	 * Stop the plugin.
	 */
	public void onDisable() {
		// Stop the web server
		WebServer.stop();
		stopTokensScheduler();

		plugin = null;
	}
	
	/**
	 * Disable the plugin.
	 */
	public static void disable() {
		Logger.warn("Disabling...");
		Logger.critical("WHY");
		
		Bukkit.getPluginManager().disablePlugin(StackIt.getPlugin());
	}
	
	/**
	 * Return the plugin variable stocked for advanced modifications on the server.
	 * 
	 * @return Plugin Plugin
	 */
	public static Plugin getPlugin() {
		return plugin;
	}

    /**
     *  Token's list emptying when tokens are outdated
     */
	private void startTokensScheduler(){
        task = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            try {
                List<Token> tokens = DatabaseManager.getTokens().getAll();
                if (!tokens.isEmpty()) {
                    for (Token token : tokens) {
                        if (!TokenManager.isTokenValid(token)) {
                            DatabaseManager.getTokens().deleteByValue(token.getValue());
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                StackIt.disable();
            }
        }, 6000L, 6000L);
    }

    private void stopTokensScheduler(){
	    getServer().getScheduler().cancelTask(task);
    }

	public static UUID UUIDbyPlayerName(String playerName){
	    return StackIt.getPlugin().getServer().getOfflinePlayer(playerName).getUniqueId();
    }

    public static boolean isPlayerOnline(UUID uuid){
        return StackIt.getPlugin().getServer().getOfflinePlayer(uuid).isOnline();
    }
}
