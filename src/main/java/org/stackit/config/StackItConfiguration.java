package org.stackit.config;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.stackit.src.Language;
import org.stackit.src.Logger;
import org.stackit.src.StackIt;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class StackItConfiguration {
	public static File configFile = new File(StackIt.getPlugin().getDataFolder(), "StackIt.yml");
	public static FileConfiguration config = null;

	/**
	 * Initiate the configuration file when the server is starting.
	 * Required for the server to work properly.
	 */
	public static void init() {
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				
				URL originalFile = StackIt.class.getResource("/ConfigurationFiles/StackIt.yml");
				FileUtils.copyURLToFile(originalFile, configFile);
			} catch (IOException e) {
				Logger.error(Language.process(Language.get(Language.getBukkitLanguage(), "main_config_error")));
				e.printStackTrace();
			}
		}
		
		config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	/**
	 * Save the file.
	 * Require when setting the value of an argument in the configuration file.
	 * @return Boolean
	 */
	public static void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ######################################
	 * #			   GENERAL  			#
	 * ######################################
	 */

	/**
	 * Check if the plugin is enabled
	 * @return Boolean
	 */
	public static void checksEnabled() throws StackitDisabledException {
	    if (!config.getBoolean("StackIt.enabled")) {
            throw new StackitDisabledException("Plugin disabled");
        }
	}
	
	/**
	 * Enable or disable the plugin in the configuration file.
	 * /!\ This will only take effect on the next restart of the server /!\
	 * 
	 * @param Boolean
	 */
	public static void setEnabled(Boolean enabled) {
		config.set("StackIt.enabled", enabled);
		save();
	}
	
	/**
	 * Get the prefix of the plugin.
	 * @return String Prefix
	 */
	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', config.getString("StackIt.prefix")) + " " + ChatColor.RESET;
	}
	
	/**
	 * Set the prefix of the plugin.
	 * @param String Prefix
	 */
	public static void setPrefix(String prefix) {
		config.set("StackIt.prefix", prefix);
		save();
	}
	
	/**
	 * Set the status of the log system.
	 * @param Boolean Enabled
	 */
	public static void setLogEnabled(Boolean enabled) {
		config.set("StackIt.log", enabled);
		save();
	}
	
	/**
	 * Check if the plugin is in maintenance
	 * @return Boolean Maintenance
	 */
	public static Boolean isInMaintenance() {
		return config.getBoolean("StackIt.maintenance.enabled");
	}
	
	/**
	 * Set the plugin in maintenance.
	 * @param Boolean Maintenance
	 */
	public static void setInMaintenance(Boolean maintenance) {
		config.set("StackIt.maintenance.enabled", maintenance);
		save();
	}
	
	/**
	 * Get the API maintenance error.
	 * @return String API maintenance error
	 */
	public static String getAPIMaintenanceError() {
		return ChatColor.translateAlternateColorCodes('&', config.getString("StackIt.maintenance.messages.api"));
	}
	
	/**
	 * Set the API maintenance error.
	 * @param String API maintenance error
	 */
	public static void setAPIMessageError(String error) {
		config.set("StackIt.maintenance.messages.api", error);
		save();
	}
	
	/**
	 * Get the API maintenance error.
	 * @return String API maintenance error
	 */
	public static String getInGameMaintenanceError(String language) {
		return ChatColor.translateAlternateColorCodes('&', config.getString("StackIt.maintenance.messages.ingame." + language));
	}
	
	/**
	 * Set the in-game maintenance error.
	 * @param String In-game maintenance error
	 */
	public static void setInGameMessageError(String error) {
		config.set("StackIt.maintenance.messages.ingame", error);
		save();
	}
	
	/**
	 * Get the SSL passphrase
	 * @return String passphrase
	 */
	public static String getSSLPassphrase() {
		return config.getString("StackIt.ssl.passphrase");
	}
	
	/**
	 * Set the SSL passphrase
	 * param String passphrase
	 */
	public static void setSSLPassphrase(String passphrase) {
		config.set("StackIt.ssl.passphrase", passphrase);
		save();
	}
	
	/*
	 * ######################################
	 * #			   DATABASE  			#
	 * ######################################
	 */
	
	/**
	 * Get the host of the database.
	 * @return String
	 */
	public static String getDatabaseHost() {
		return config.getString("StackIt.database.host");
	}
	
	/**
	 * Set the host of the database.
	 * @argument String
	 */
	public static void setDatabaseHost(String host) {
		config.set("StackIt.database.host", host);
		save();
	}
	
	/**
	 * Get the name of the database.
	 * @return String
	 */
	public static String getDatabaseName() {
		return config.getString("StackIt.database.name");
	}
	
	/**
	 * Set the name of the database.
	 * @argument String
	 */
	public static void setDatabaseName(String name) {
		config.set("StackIt.database.name", name);
		save();
	}
	
	/**
	 * Get the user name of the database.
	 * @return String
	 */
	
	public static String getDatabaseUser() {
		return config.getString("StackIt.database.username");
	}
	
	/**
	 * Set the user name of the database.
	 * @argument String
	 */
	public static void setDatabaseUser(String user) {
		config.set("StackIt.database.username",  user);
		save();
	}
	
	/**
	 * Get the user password of the database.
	 * @return String
	 */
	public static String getDatabasePassword() {
		return config.getString("StackIt.database.password");
	}
	
	/**
	 * Set the user password of the database.
	 * @return String
	 */
	public static void setDatabasePassword(String password) {
		config.set("StackIt.database.password", password);;
		save();
	}

	/**
	 * Get the database type.
	 * @return String
	 */
	public static String getDatabaseType() {
		return config.getString("StackIt.database.type");
	}
	
	/**
	 * Set the database type in the configuration files.
	 * @argument String
	 */
	public static void setDatabaseType(String type) {
		config.set("StackIt.database.type", type);
		save();
	}
	
	/**
	 * Get the database port.
	 * @return Integer
	 */
	public static Integer getDatabasePort() {
		return config.getInt("StackIt.database.port");
	}
	
	/**
	 * Set the port of the database.
	 * @param Integer
	 */
	public static void setDatabasePort(Integer port) {
		config.set("StackIt.database.port", port);
		save();
	}
	
	/**
	 * Get the prefix for the plugin tables.
	 * @return String
	 */
	public static String getDatabaseTablePrefix() {
		return config.getString("StackIt.database.prefix");
	}
	
	/**
	 * Set the prefix for the plugin tables.
	 * @param String
	 */
	public static void setDatabaseTablePrefix(String prefix) {
		config.set("StackIt.database.prefix", prefix);
		save();
	}

	public static String getQueueDAOClassName(){
		return config.getString("StackIt.database.dao.queue");
	}

	public static String getLogsDAOClassName() {
		return config.getString("StackIt.database.dao.logs");
	}

    public static String getUsersDAOClassName(){
        return config.getString("StackIt.database.dao.users");
    }

    public static String getTokensDAOClassName(){
        return config.getString("StackIt.database.dao.tokens");
    }
	
	/*
	 * ######################################
	 * #			     API    			#
	 * ######################################
	 */
	
	/**
	 * Get the port of the API.
	 * @return Integer
	 */
	public static Integer getAPIPort() {
		return config.getInt("StackIt.api.port");
	}
	
	/**
	 * Set the port of the API.
	 * @argument INteger
	 */
	public static void setAPIPort(Integer port) {
		config.set("StackIt.api.port", port);
		save();
	}
	
	/*
	 * ######################################
	 * #	     INTERNATIONALIZATION    	#
	 * ######################################
	 */
	
	/**
	 * Check if the use of a language is forced
	 * @return Boolean Forced
	 */
	public static Boolean isLanguageForced() {
		return config.getBoolean("StackIt.internationalization.forced");
	}
	
	/**
	 * Set the use of a language forced
	 * @param Boolean Forced
	 */
	public static void setLanguageForced(Boolean forced) {
		config.set("StackIt.internationalization.forced", forced);
		save();
	}
	
	/**
	 * Get the forced language
	 * @return String Language
	 */
	public static String getForcedLanguage() {
		return config.getString("StackIt.internationalization.forced_language");
	}
	
	/**
	 * Set the forced language
	 * @return Boolean Forced
	 */
	public static void setForcedLanguage(String language) {
		config.set("StackIt.internationalization.forced_language", language);
		Language.userLanguage = language;
		save();
	}
	
	/**
	 * Get the default language if user's language is not supported
	 * @return String Language
	 */
	public static String getDefaultLanguage() {
		return config.getString("StackIt.internationalization.default_language");
	}
	
	/**
	 * Set the default language if user's language is not supported
	 * @param String Language
	 */
	public static void setDefaultLanguage(String language) {
		config.set("StackIt.internationalization.default_language", language);
		save();
	}
}