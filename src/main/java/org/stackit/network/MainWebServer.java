package org.stackit.network;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;
import org.stackit.src.Language;
import org.stackit.src.Logger;
import org.stackit.src.PermissionManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsServer;

public class MainWebServer {
	private static HttpsServer httpserv = null;
	
	/**
	 * Initiate the web server of the plugin.
	 * Essential for the connection to the CMS.
	 * 
	 * @return Boolean
	 */
	public static Boolean init() {
		System.out.println(" ");
		System.out.println("|=========- StackIt Webserver Manager -=========|");
		
		MainWebServer.open();
		MainWebServer.configure();
		
		// Start the server
		MainWebServer.start();
		Logger.info(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "webserver_started")));
		
		return true;
	}
	
	/**
	 * Start the web server.
	 */
	public static void start() {
		httpserv.setExecutor(null);
		httpserv.start();
	}
	
	/**
	 * Stop the web server.
	 */
	public static void stop() {
		if(httpserv != null) {
			httpserv.stop(0);	
		}
	}
	
	/**
	 * Initiate the web server variable and verifies if the port is available for use.
	 * @return Boolean
	 */
	private static Boolean open() {
		try {
			// Create the web server at the said port.
			httpserv = HttpsServer.create(new InetSocketAddress(StackItConfiguration.getAPIPort()), 0);
			
			return true;
		} catch (Exception e) {
			// If an error occur, report it in the console.
			Logger.critical(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "error_initiating_webserver")));
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Create the links for the web server.
	 */
	private static void configure() {
		httpserv.createContext("/", new HandlerWebServer());
		
		// Add the minimal pages to the system.
		HandlerWebServer.addHandler("/login", new PageConnect());
		HandlerWebServer.addHandler("/debug", new PageDebug());
		HandlerWebServer.addHandler("/give", new PageGive());
		
		if(!PermissionManager.contextHasPermissions("/debug")) {
			PermissionManager.createContext("/debug", "debug");
		}
		
		if(!PermissionManager.contextHasPermissions("/give")) {
			PermissionManager.createContext("/give", "give");
		}
	}
	
	public static HttpExchange setHeaders(HttpExchange exchange) {
		Headers headers = exchange.getResponseHeaders();
		
		headers.add("Content-Type", "application/json");
		headers.add("Application-Name", "StackIt");
		headers.add("Application-Version", "v1");
		headers.add("Application-Author", "Shamelin && Marethyun (Uphoria.org)");
		
		return exchange;
	}
	
	/**
	 * Return a JSON Object with the arguments provided.
	 * Used for answers to the client.
	 * @param args
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject output(HashMap<String, Object> args) {
		JSONObject output = new JSONObject();
		
		output.put("result", args);
		output.put("Application-Name", "StackIt");
		output.put("Application-Version", "v1");
		output.put("Application-Author", "DedPlay (Tardis Project)");
		
		return output;
	}
	
	/**
	 * Get one argument of the exchange.
	 * @param exchange
	 * @param key
	 * @return String
	 */
	public static String GETO(HttpExchange exchange, String key) {
		String query = exchange.getRequestURI().getQuery();
		
		if(query != null) {
			for(String param : query.split("&")) {
			  String pair[] = param.split("=");
			  
			  if(pair.length >= 2 && pair[0].equalsIgnoreCase(key)) {
				  return pair[1];
			  }
			}
		}
		
		return null;
	}
	
	/**
	 * Get all the _GET arguments of the exchange.
	 * @param exchange
	 * @return HashMap<String, String>
	 */
	public static HashMap<String, String> GETALL(HttpExchange exchange) {
		String query = exchange.getRequestURI().getQuery();
		HashMap<String, String> GET = new HashMap<String, String>();
		
		if(query != null) {
			for(String param : query.split("&")) {
				String pair[] = param.split("=");
				
				if(pair.length >= 2) {
					GET.put(pair[0], pair[1]);
				} else {
					GET.put(pair[0], "");
				}
			}
		}
		
		return GET;
	}
}
