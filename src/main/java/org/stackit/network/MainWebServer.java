package org.stackit.network;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsServer;
import org.json.simple.JSONObject;
import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.StackIt;
import org.stackit.config.LanguageConfiguration;

import java.util.HashMap;

import static spark.Spark.*;

public class MainWebServer {
	private static HttpsServer httpserv = null;
	
	/**
	 * Initiate the web server of the plugin.
	 * Essential for the connection to the CMS.
	 * 
	 * @return Boolean
	 */
	public static void init() {
        StackIt.getPlugin().getLogger().info("Initializing API server..");
		
		MainWebServer.open();
//		MainWebServer.configure(); TODO REWORK THIS
		
		// Start the server
		Logger.info(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "webserver_started")));
	}

	
	/**
	 * Stop the web server.
	 */
	public static void stop() {
        stop();
	}
	
	/**
	 * Initiate the web server variable and verifies if the port is available for use.
	 */
	private static void open() {
		try {
			// Create the web server at the said port.
			secure("ssl.keystore", "stackit-sslpass", null, null);
			port(25177);
			get("/hello", ((request, response) -> "Hello world"));

		} catch (Exception e) {
			// If an error occur, report it in the console.
			Logger.critical(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "error_initiating_webserver")));
			e.printStackTrace();
		}
	}

	public static HttpExchange setHeaders(HttpExchange exchange) {
		Headers headers = exchange.getResponseHeaders();
		
		headers.add("Content-Type", "application/json");
		headers.add("Application-Name", "StackIt");
		headers.add("Application-Version", "v1");
		headers.add("Application-Author", "Shamelin & Marethyun (Uphoria.org)");
		
		return exchange;
	}
	
	/**
	 * Return a JSON Object with the arguments provided.
	 * Used for answers to the client.
	 * @param args
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject translateJson(HashMap<String, Object> args) {
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
