package org.stackit.database;

import java.util.HashMap;

import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;
import org.stackit.src.Language;
import org.stackit.src.Logger;
import org.stackit.src.StackIt;

public class Database {
	protected static HashMap<String, DatabaseType> supported = new HashMap<>();
	
	/**
	 * Initiate the database connection and check if the type of database is supported
	 * by the plugin.
	 * @return 
	 */
	public static boolean init() {
		// Create the array of supported database types and insert the official ones.
		Database.addSupportedDatabaseType("mysql", "org.stackit.database.MySQLClient");
		if(StackItConfiguration.isLogEnabled()) {
			System.out.println(" ");
			System.out.println("|=========- StackIt Database Manager -=========|");
			Logger.info(Language.replace(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "dtb_type")), "TYPE", StackItConfiguration.getDatabaseType()));
		}
		if(Database.supported(StackItConfiguration.getDatabaseType().toLowerCase())) {
			Logger.info(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "initiating_dtb_connect")));
			DatabaseType database = Database.getSupportedDatabaseTypes().get(StackItConfiguration.getDatabaseType().toLowerCase());
			
			if(database.Connect() == null) {
				StackIt.disable();
				return false;
			} else {
				if(StackItConfiguration.isLogEnabled()) {
					Logger.info(LanguageConfiguration.get(Language.getBukkitLanguage(), "checking_dtb_problems"));
				}
				
				database.CheckDatabase();
				
				if(StackItConfiguration.isLogEnabled()) Logger.info(LanguageConfiguration.get(Language.getBukkitLanguage(), "done"));
				return true;
			}
		} else {
			Logger.warn(LanguageConfiguration.get(Language.getBukkitLanguage(), "dtb_type_not_supported"));
			return false;
		}
	}
	
	/**
	 * Get the supported database types.
	 * @return String[]
	 */
	public static HashMap<String, DatabaseType> getSupportedDatabaseTypes() {
		return Database.supported;
	}
	
	/**
	 * Get a database type from the list.
	 * @param type
	 * @return DatabaseType
	 */
	public static DatabaseType getDatabaseType(String type) {
		return Database.supported.get(type.toLowerCase());
	}
	
	/**
	 * Return the active database used in the plugin.
	 * Can be set in the main configuration file.
	 * @return DatabaseType
	 */
	public static DatabaseType getActiveDatabase() {
		return getDatabaseType(StackItConfiguration.getDatabaseType());
	}
	
	/**
	 * Add a database type to the plugin.
	 * @param type, className
	 */
	public static void addSupportedDatabaseType(String type, String className) {
		ClassLoader classLoader = StackIt.class.getClassLoader();
		
		if(!Database.getSupportedDatabaseTypes().containsKey(type.toLowerCase())) {
		    try {
		        Class<?> dtbctype = classLoader.loadClass(className);
		        DatabaseType dtbtype = null;
		        
				try {
					dtbtype = (DatabaseType) dtbctype.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
		        
		        Database.supported.put(type, dtbtype);
		    } catch (ClassNotFoundException e) {
		    	Logger.warn(Language.replace(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "dtb_type_not_supported")), "{DTB_TYPE_CLASS}", className));
		    }
		}
	}
	
	/**
	 * Check if the database is supported by the plugin.
	 * @param database
	 * @return Boolean
	 */
	public static Boolean supported(String database) {
		if(Database.getSupportedDatabaseTypes().containsKey(database)) {
			return true;
		}
		
		return false;
	}

}
