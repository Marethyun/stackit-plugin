package org.stackit.src;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.stackit.config.StackItConfiguration;
import org.stackit.database.Database;

public class Logger {
	
	/**
	 * Initiate the logger of the plugin.
	 */
	public static void init() {
		try {
			// Change encoding to support accents (é, è, â, à, ä, etc.)
			System.setOut(new PrintStream(System.out, true, "ibm850"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an information to the console.
	 * Default and most basic output to the console.
	 * @param message
	 */
	public static void info(String message) {
		System.out.println("[StackIt] [INFO] " + message);
	}
	
	/**
	 * Send a warning to the console.
	 * Useful when a plugin use a method that is deprecated.
	 * @param message
	 */
	public static void warn(String message) {
		System.out.println("[StackIt] [WARN] " + message);
	}
	
	/**
	 * Send an error to the console.
	 * When an error occur in the plugin. If the error makes the plugin shut down, use critical()
	 * @param message
	 */
	public static void error(String message) {
		System.out.println("[StackIt] [ERROR] " + message);
	}
	
	/**
	 * Send a critical alert to the console.
	 * @param message
	 */
	public static void critical(String message) {
		System.out.println("[StackIt] [CRITICAL] " + message);
	}
	
	/**
	 * Add a simple log to the database.
	 * @param String Log
	 * @param String Executor
	 */
	public static void pushLog(String log, String executor, String context, String remoteAddress) {
		if(StackItConfiguration.isLogEnabled()) {
			Date date = new Date();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			
			String dateOutput = dateFormat.format(date);
			String timeOutput = timeFormat.format(date);
			
			Database.getActiveDatabase().AddLog(dateOutput, timeOutput, log, executor, context, remoteAddress);
		}
	}
	
	/**
	 * Add a context executed log to the database.
	 * @param String Context executed
	 * @param String Executor
	 */
	public static void pushContextLog(String context, String executor, String remoteAddress) {
		if(StackItConfiguration.isLogEnabled()) {
			Date date = new Date();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			
			String dateOutput = dateFormat.format(date);
			String timeOutput = timeFormat.format(date);
			
			String log = "Context \"" + context + "\" executed";
			
			Database.getActiveDatabase().AddLog(dateOutput, timeOutput, log, executor, context, remoteAddress);
		}
	}
	
	/**
	 * Add a user connected log to the database.
	 * @param remoteAddress 
	 * @param String Context executed
	 * @param String Executor
	 */
	public static void pushUserLoggedInLog(String user, String context, String remoteAddress) {
		if(StackItConfiguration.isLogEnabled()) {
			Date date = new Date();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			
			String dateOutput = dateFormat.format(date);
			String timeOutput = timeFormat.format(date);
			
			String log = "User \"" + user + "\" successfully connected to the API";
			
			Database.getActiveDatabase().AddLog(dateOutput, timeOutput, log, user, context, remoteAddress);
		}
	}
	
	/**
	 * Add a user tried to log in log to the database.
	 * @param String Context executed
	 * @param String Executor
	 */
	public static void pushUserTriedToLogInLog(String user, String context, String error, String remoteAddress) {
		if(StackItConfiguration.isLogEnabled()) {
			Date date = new Date();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			
			String dateOutput = dateFormat.format(date);
			String timeOutput = timeFormat.format(date);
			
			String log = "User \"" + user + "\" tried to connect to the API (Error: " + error + ")";
			
			Database.getActiveDatabase().AddLog(dateOutput, timeOutput, log, user, context, remoteAddress);
		}
	}

}
