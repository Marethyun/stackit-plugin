package org.stackit;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.config.StackItConfiguration;
import org.stackit.config.StackitDisabledException;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.Token;
import org.stackit.network.MainWebServer;

import java.util.List;

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

			// Load language configuration files
			LanguageManager.init();
			Language.init();

			// Start the API
			MainWebServer.init();

			// Set the commands
            // getCommand("stackit").setExecutor(new StackItCommand()); TODO REWORK THIS

			// Register the events
            // registerEvents(new PlayerJoinEvent()); TODO REWORK THIS

			startTokensScheduler();

			Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "plugin_initialized")));
			Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "plugin_initialized_2")));
		} catch (StackitDisabledException e){
		    Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "plugin_disabled")));
		    StackIt.disable();
		} catch (Exception e){
			StackIt.disable();
		}
	}
	
	/**
	 * Stop the plugin.
	 */
	public void onDisable() {
		// Stop the web server
		MainWebServer.stop();
		stopTokensScheduler();

		plugin = null;
	}
	
	/**
	 * Disable the plugin.
	 */
	public static void disable() {
		Logger.warn(Language.process(Language.get(Language.getBukkitLanguage(), "disabling_plugin")));
		
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
     * For each token: Checks token' expiration time and delete it when it's outdated.
     * Every 10 minecraft ticks (average 500ms)
     */
	private void startTokensScheduler(){
        task = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    List<Token> tokens = DatabaseManager.getTokens().getAll();
                    if (!tokens.isEmpty()) {
                        long expiration = StackItConfiguration.getTokensExpiration();
                        System.out.println(expiration);
                        for (Token token : tokens) {
                            if (token.getTime() + expiration >= System.currentTimeMillis()) {
                                DatabaseManager.getTokens().deleteByValue(token.getValue());
                            }
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 0L, 10L);
    }

    private void stopTokensScheduler(){
	    getServer().getScheduler().cancelTask(task);
    }
	
	/**
	 * Register all the events of the plugin.
	 */
	public void registerEvents(Listener... listener) {
		for(Integer i = 0 ; i < listener.length ; i++) {
			Bukkit.getPluginManager().registerEvents(listener[i], getPlugin());
		}
	}
}
