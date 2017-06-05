package org.stackit.src;

// TODO fix database errors
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.stackit.commands.StackItCommand;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.DatabaseManager;
import org.stackit.events.PlayerJoinEvent;
import org.stackit.network.MainWebServer;

/**
 * StackIt: A powerful shop for Minecraft 1.7-1.11
 * 
 * @author shamelin
 */
public class StackIt extends JavaPlugin {
	static Plugin plugin = null;
	static DatabaseManager dbManager;

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
			StackItConfiguration.init(); // TODO check if config is loaded

			// Load language configuration files
			LanguageManager.init();
			Language.init();

			if (StackItConfiguration.isLogEnabled()) {
				Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "debug_enabled")));
			} else {
				Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "debug_disabled")));
			}

			// Load the give manager
			GiveManager.init();

			// Start the API
			MainWebServer.init();

			// Set the commands
			getCommand("stackit").setExecutor(new StackItCommand());

			// Register the events
			registerEvents(new PlayerJoinEvent());

			Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "plugin_initialized")));
			Logger.info(Language.process(Language.get(Language.getBukkitLanguage(), "plugin_initialized_2")));
			Logger.critical(Language.process(Language.get(Language.getBukkitLanguage(), "error_loading_plugin")));
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
	 * Register all the events of the plugin.
	 */
	public void registerEvents(Listener... listener) {
		for(Integer i = 0 ; i < listener.length ; i++) {
			Bukkit.getPluginManager().registerEvents(listener[i], getPlugin());
		}
	}
}
