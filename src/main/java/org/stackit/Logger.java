package org.stackit;

import org.stackit.database.DatabaseManager;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

// TODO rework this
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
	public static void pushLog(String log, String executor, String context) {
        Date date = new Date(System.currentTimeMillis());

        DatabaseManager.getLogs().addLog(date, log, context);
	}
	
	/**
	 * Add a user connected log to the database.
     * @param remoteAddress
     * @param String Context executed
     * @param String Executor
     */
	public static void userLoggedIn(String user, String context) {
		Date date = new Date(System.currentTimeMillis());

		String log = "User \"" + user + "\" successfully connected to the API";

		DatabaseManager.getLogs().addLog(date, log, context);
	}
	
	/**
	 * Add a user tried to log in log to the database.
     * @param String Context executed
     * @param String Executor
     */
	public static void userTriedToLog(String user, String context, String error) {
		Date date = new Date(System.currentTimeMillis());
		String log = "User \"" + user + "\" tried to connect to the API (Error: " + error + ")";
		DatabaseManager.getLogs().addLog(date, log, context);
	}

}
