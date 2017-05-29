package org.stackit.src;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;

import net.md_5.bungee.api.ChatColor;

public class Language {
	public static String userLanguage = null;
	public static HashMap<String, Object> replacements = new HashMap<String, Object>();
	
	/**
	 * Initiate the Language class
	 */
	public static void init() {
		addReplacement("DTB_TYPE", StackItConfiguration.getDatabaseType());
		addReplacement("API_PORT", StackItConfiguration.getAPIPort());
	}
	
	/**
	 * Get the text for a key.
	 * @param String Language
	 * @param String Key
	 * @return String Text
	 */
	public static String get(String language, String key) {
		return ChatColor.translateAlternateColorCodes('&', LanguageConfiguration.get(language, key));
	}
	
	/**
	 * Get the language of Bukkit.
	 * @return String Langauge
	 */
	public static String getBukkitLanguage() {
		return userLanguage;
	}
	
	/**
	 * Get user's language.
	 * @return Player player
	 */
	public static String getPlayerLanguage(Player player) {
		Object ep;
		Field f;
		String language = null;
		
		try {
			ep = getMethod("getHandle", player.getClass()).invoke(player, (Object[]) null);
			f = ep.getClass().getDeclaredField("locale");
			
			f.setAccessible(true);
			language = (String) f.get(ep);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		return language.substring(0, 2);
	}
	
	private static Method getMethod(String name, Class<?> clazz) {
		for(Method m : clazz.getDeclaredMethods()) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		
		return null;
	}
	
	/**
	 * Add a variable to replace to text.
	 * @param String To be replaced
	 * param Object Replaced by
	 */
	public static void addReplacement(String replace, Object replacedBy) {
		if(!replacements.containsKey(replace)) {
			replacements.put(replace.toLowerCase(), replacedBy);
		}
	}
	
	/**
	 * Replace the variables with text.
	 * @return String Text
	 * @param String Text
	 */
	public static String process(String text) {
		if(text == null) {
			return null;
		}
		
		Pattern pattern = Pattern.compile("\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(text);
		StringBuffer buffer = new StringBuffer();
		
		while(matcher.find()) {
			Object replacement = replacements.get(matcher.group(1).toLowerCase());
			
			if(replacement != null) {
				matcher.appendReplacement(buffer, replacement.toString());
			}
		}
		
		matcher.appendTail(buffer);
		return buffer.toString();
	}
	
	/**
	 * Replace the variables with by text.
	 * @return String Text
	 * @return String Replace
	 * @return String ReplacedBy
	 * @param String Text
	 */
	public static String replace(String text, String replace, String replaceBy) {
		Pattern pattern = Pattern.compile("\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(text);
		StringBuffer buffer = new StringBuffer();
		
		while(matcher.find()) {
			if(replaceBy != null) {
				matcher.appendReplacement(buffer, replaceBy.toString());
			}
		}
		
		matcher.appendTail(buffer);
		return buffer.toString();
	}

}
