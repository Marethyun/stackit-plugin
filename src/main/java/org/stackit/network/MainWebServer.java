package org.stackit.network;

import com.sun.net.httpserver.*;
import org.json.simple.JSONObject;
import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.StackIt;
import org.stackit.config.LanguageConfiguration;
import org.stackit.config.StackItConfiguration;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;

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
		MainWebServer.configure();
		
		// Start the server
		MainWebServer.start();
		Logger.info(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "webserver_started")));
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
	 */
	private static void open() {
		try {

            char[] password = StackItConfiguration.getSSLPassphrase().toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");

            // Create the web server at the said port.
            httpserv = HttpsServer.create(new InetSocketAddress(StackItConfiguration.getAPIPort()), 0);
            File keyFile = new File(StackIt.getPlugin().getDataFolder(), "ssl.keystore");
            if (!keyFile.exists()){
                throw new Exception("Failed to find keyStore file, please add one");
            }
            FileInputStream fis = new FileInputStream(keyFile);
            ks.load(fis, password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            httpserv.setHttpsConfigurator(new HttpsConfigurator(sslContext){
                @Override
                public void configure(HttpsParameters httpsParameters) {
                    try {
                        SSLEngine engine = sslContext.createSSLEngine();
                        engine.setEnabledProtocols(new String[] {"SSLv3"});
                        httpsParameters.setNeedClientAuth(false);
                        httpsParameters.setCipherSuites(engine.getEnabledCipherSuites());
                        httpsParameters.setProtocols(engine.getEnabledProtocols());

                        SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                        httpsParameters.setSSLParameters(defaultSSLParameters);

                    } catch (Exception e){
                        e.printStackTrace();
                        Logger.error(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "error_initiating_webserver")));
                        StackIt.disable();
                    }
                }
            });


		} catch (Exception e) {
			// If an error occur, report it in the console.
			Logger.critical(Language.process(LanguageConfiguration.get(Language.getBukkitLanguage(), "error_initiating_webserver")));
			e.printStackTrace();
		}
	}

	/**
	 * Create the links for the web server.
	 */
	private static void configure() {
	    Logger.info("Initializing webserver routes..");
	    // Set handler
		httpserv.createContext("/", new HandlerWebServer());

		// Routes
		HandlerWebServer.addHandler("/connect", new PageConnect());
	}
	
	public static HttpExchange setHeaders(HttpExchange exchange) {
	    HttpsExchange httpsExchange = (HttpsExchange) exchange;
		Headers headers = httpsExchange.getResponseHeaders();

		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Content-Type", "application/json");
		headers.add("Application-Name", "StackIt");
		headers.add("Application-Version", "1.3.14");
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
