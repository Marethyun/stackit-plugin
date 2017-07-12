package org.stackit.network;

import org.json.simple.JSONObject;
import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import spark.Response;
import spark.Spark;

import java.util.HashMap;

public class WebServer {
	private static Spark spark;
	
	/**
	 * Initiate the web server of the plugin.
	 * Essential for the connection to the CMS.
	 * 
	 * @return Boolean
	 */
	public static void init() {
		WebServer.open();
	}

	
	/**
	 * Stop the web server.
	 */
	public static void stop() {
	    if (spark != null) {
            spark.stop();
        }
	}
	
	/**
	 * Initiate the web server variable and verifies if the port is available for use.
	 */
	private static void open() {
	    Logger.info("Initializing web interface..");
		try {
			spark = new Spark(false);
			
			// Create the web server at the said port.
//			spark.secure("ssl.keystore", "stackit-sslpass", null, null);
			spark.port(StackItConfiguration.getAPIPort());
			spark.init();

			spark.get("*", (req, res) -> {
				//return "Hello world!";
				return WebHandler.handle(req, res);
			});

		} catch (Exception e) {
			// If an error occur, report it in the console.
			Logger.critical("Error while initializing web interface..");
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
