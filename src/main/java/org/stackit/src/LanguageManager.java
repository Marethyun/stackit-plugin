package org.stackit.src;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;

public class LanguageManager {
	
	/**
	 * Initiate the language manager.
	 */
	public static void init() {
		Logger.info("Loading internationalization...");
		
		getDataFolder().mkdirs();
		loadMinimal();
		
		// If a language has been forced to be used
		if(StackItConfiguration.isLanguageForced()) {
			Logger.info("Forced language detected. Applying.");
			Language.userLanguage = StackItConfiguration.getForcedLanguage();
		} else { // No language forced, use the computer's one.
			Language.userLanguage = System.getProperty("user.language");
		}
		
		System.out.println(" ");
		Logger.info("Server language detected: " + Language.userLanguage);
		Logger.info("Applying language for the future messages...");
	}
	
	/**
	 * Check if the ConfigurationFiles folder exist.
	 * @return Boolean Exists.
	 */
	public static Boolean folderExist() {
		if(getDataFolder().exists()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Load the minimal languages of the plugin.
	 */
	public static void loadMinimal() {
		addLanguage("fr");
		addLanguage("en");
	}
	
	/**
	 * Get the folder where the configuration files are.
	 * @return File
	 */
	public static File getDataFolder() {
		return new File(StackIt.getPlugin().getDataFolder() + File.separator + "Languages" + File.separator);
	}
	
	/**
	 * Add a language.
	 * @param String language
	 */
	public static void addLanguage(String language) {
		Logger.info("Adding support of '" + language + "' to the plugin...");
		
		File configFile = new File(getDataFolder(), language + ".yml");
		
		// If the language file has not been copied or doesn't exist
		if(!configFile.exists() || configFile.length() <= 0) {
			try {
				configFile.createNewFile();
				
				URL originalFile = StackIt.class.getResource("/Languages/" + language + ".yml");
				FileUtils.copyURLToFile(originalFile, configFile);
			} catch (IOException e) {
				Logger.critical("An error occured while creating the main configuration file:");
				e.printStackTrace();
			}
		}
		
		LanguageConfiguration.addLanguage(language, configFile);
	}
}
