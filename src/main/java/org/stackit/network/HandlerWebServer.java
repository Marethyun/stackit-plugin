package org.stackit.network;

import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;

public class HandlerWebServer {
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
	 * @throws IOException
	 */
	protected static String handle(Request request, Response response) {
	    if (!StackItConfiguration.isInMaintenance()) {
            // If the page exist and has been set in the script.
            if (pageExist(request.uri())) {
            	response = MainWebServer.setHeaders(response);
                Page page = getHandler(request.uri());
                HashMap<String, Object> answer = page.handle(request, response, new HashMap<String, Object>());

                String content = MainWebServer.translateJson(answer);
                response.status(200);
                return content;
            } else { // Page not found.
                HashMap<String, Object> answer = new HashMap<String, Object>();
            	response = MainWebServer.setHeaders(response);

                answer.put("status", StatusType.ERROR);
                answer.put("message", "Requested route not found");

                String content = MainWebServer.translateJson(answer);
                response.status(404);
                return content;
            }
        } else {
            HashMap<String, Object> answer = new HashMap<String, Object>();
        	response = MainWebServer.setHeaders(response);

            answer.put("status", StatusType.ERROR);
            answer.put("message", "The Stackit's API is under maintenance, please retry later");

            String content = MainWebServer.translateJson(answer);
            response.status(200);
            return content;
        }
	}
}
