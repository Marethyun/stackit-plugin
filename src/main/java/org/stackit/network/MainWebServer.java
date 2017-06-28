package org.stackit.network;

import org.json.simple.JSONObject;
import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.StackIt;
import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;
import spark.Response;
import spark.Spark;

import java.util.HashMap;

public class MainWebServer {
	private static Spark spark;
	
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
        spark.stop();
	}
	
	/**
	 * Initiate the web server variable and verifies if the port is available for use.
	 */
	private static void open() {
		try {
			//spark = new Spark(false);
			
			// Create the web server at the said port.
			spark = new Spark(false);
//			spark.secure(StackIt.getPlugin().getDataFolder().getPath() + "/ssl.jks", StackItConfiguration.getSSLPassphrase(), null, null);
			spark.port(StackItConfiguration.getAPIPort());

			spark.get("/", (req, res) -> HandlerWebServer.handle(req, res));

		} catch (Exception e) {
			// If an error occur, report it in the console.
			Logger.critical(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "error_initiating_webserver")));
			e.printStackTrace();
		}
	}

	public static Response setHeaders(Response response) {
		response.header("Content-Type", "application/json");
		response.header("Application-Name", "StackIt");
		response.header("Application-Version", "v1");
		response.header("Application-Author", "DedPlay & Marethyun (Uphoria.org)");
		
		return response;
	}
	
	/**
	 * Return a JSON Object with the arguments provided.
	 * Used for answers to the client.
	 * @param args
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static String translateJson(HashMap<String, Object> args) {
		JSONObject output = new JSONObject();
		output.put("query", args);
		return output.toJSONString();
	}
}
