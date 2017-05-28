package org.stackit.config;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageConfiguration {
	public static HashMap<String, File> languagesFiles = new HashMap<String, File>();
	public static HashMap<String, FileConfiguration> languagesConfigurations = new HashMap<String, FileConfiguration>();
	
	/**
	 * Get all the language files available.
	 * @return HashMap<String, File> Language Files
	 */
	public static HashMap<String, File> getLanguagesFiles() {
		return languagesFiles;
	}
	
	/**
	 * Get all the language configurations available.
	 * @return HashMap<String, File> Language Files
	 */
	public static HashMap<String, FileConfiguration> getLanguagesConfigurations() {
		return languagesConfigurations;
	}
	
	/**
	 * Get a particular file.
	 * @param String Language
	 * @return File File
	 */
	public static File getLanguageFile(String language) {
		return getLanguagesFiles().get(language);
	}
	
	/**
	 * Get a particular FileConfiguration.
	 * @param String Language
	 * @return FileConfiguration FileConfiguration
	 */
	public static FileConfiguration getLanguageConfig(String language) {
		return getLanguagesConfigurations().get(language);
	}
	
	/**
	 * Add a language.
	 * @param String Language name
	 * @param File Language file
	 */
	public static void addLanguage(String languageName, File language) {
		languagesFiles.put(languageName, language);
		languagesConfigurations.put(languageName, YamlConfiguration.loadConfiguration(language));
	}
	
	/**
	 * Get a language text.
	 * @param String Language
	 * @param String Key
	 * @return String Text
	 */
	public static String get(String language, String key) {
		try {
			FileConfiguration textConfig = getLanguageConfig(language);
			String text = null;
			
			if(textConfig != null) {
				text = textConfig.getString(key);
			}
			
			// If the text is null, set the language to the default one
			if(text == null) {
				String defaultLanguage = StackItConfiguration.getDefaultLanguage();
				text = getLanguageConfig(defaultLanguage).getString(key);
				
				// If language is not supported
				if(text == null) {
					text = getLanguageConfig("en").getString(key);
				}
			}
			
			if(text == null) {
				return "null";
			}
			
			return text;
		} catch(Exception e) {
			return "null";
		}
	}
}
