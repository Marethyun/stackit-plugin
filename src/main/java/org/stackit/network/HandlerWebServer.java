package org.stackit.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

public class HandlerWebServer implements HttpHandler {
	public static HashMap<String, Page> pages = new HashMap<String, Page>();
	
	/**
	 * Get all the pages available on the webserver.
	 * @return HashMap<String, Page> Pages
	 */
	public static HashMap<String, Page> getPages() {
		return pages;
	}
	
	/**
	 * Check if page exist.
	 * @param String Path
	 * @return Boolean Exists
	 */
	public static Boolean pageExist(String path) {
		if(pages.containsKey(path)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Add a page to the webserver.
	 * @param String Path
	 * @param Page Handler
	 */
	public static void addHandler(String path, Page handler) {
		if(!pageExist(path)) {
			pages.put(path, handler);
			
			Logger.info(Language.replace(Language.process(Language.get(Language.getBukkitLanguage(), "webserver_context_created")), "CONTEXT", path));
		}
	}
	
	/**
	 * Get the handler of a page.
	 * @param String Path
	 * @return Page Handler
	 */
	public static Page getHandler(String path) {
		return pages.get(path);
	}

	/**
	 * Main server manipulator.
	 * Depending on the path the client is, the server will offer a
	 * different response by calling the handler.
	 * 
	 * Do not modify this section. If you want to add pages, please
	 * use the method addHandler(). The method below will automati-
	 * ally redirect the user to the handler you've created.
	 * 
	 * @param HttpExchange Exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
	    if (!StackItConfiguration.isInMaintenance()) {
            URI query = exchange.getRequestURI();
            // If the page exist and has been set in the script.
            if (pageExist(query.getPath())) {
                Page page = getHandler(query.getPath());
                HashMap<String, Object> answer = page.handle(exchange, new HashMap<String, Object>());

                JSONObject content = MainWebServer.translateJson(answer);
                exchange.sendResponseHeaders(200, content.toJSONString().length());

                OutputStream os = exchange.getResponseBody();
                os.write(content.toJSONString().getBytes());
                os.close();
            } else { // Page not found.
                HashMap<String, Object> answer = new HashMap<String, Object>();
                exchange = MainWebServer.setHeaders(exchange);

                answer.put("status", StatusType.ERROR);
                answer.put("message", "The requested route not found");

                JSONObject content = MainWebServer.translateJson(answer);
                exchange.sendResponseHeaders(404, content.toJSONString().length());

                OutputStream os = exchange.getResponseBody();
                os.write(content.toJSONString().getBytes());
                os.close();
            }
        } else {
            HashMap<String, Object> answer = new HashMap<String, Object>();
            exchange = MainWebServer.setHeaders(exchange);

            answer.put("status", StatusType.ERROR);
            answer.put("message", "The Stackit's API is under maintenance, please retry later");

            JSONObject content = MainWebServer.translateJson(answer);
            exchange.sendResponseHeaders(200, content.toJSONString().length());

            OutputStream os = exchange.getResponseBody();
            os.write(content.toJSONString().getBytes());
            os.close();
        }
	}
}
