package org.stackit.network;

import org.stackit.Language;
import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import org.stackit.network.pages.Page;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;

public class HandlerWebServer {
	public static HashMap<String, Class<? extends Page>> pages = new HashMap<>();
	
	/**
	 * Get all the pages available on the webserver.
	 * @return HashMap<String, Page> Pages
	 */
	public static HashMap<String, Class<? extends Page>> getPages() {
		return pages;
	}
	
	/**
	 * Check if page exist.
	 * @param String Path
	 * @return Boolean Exists
	 */
	public static Boolean pageExist(String path) {
        return pages.containsKey(path);
    }
	
	/**
	 * Add a page to the webserver.
	 * @param String Path
	 * @param Page Handler
	 */
	public static void addHandler(String path, Class<? extends Page> handler) {
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
	public static Class<? extends Page> getHandler(String path) {
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
	protected static String handle(Request request, Response response) throws Exception {
	    if (!StackItConfiguration.isInMaintenance()) {
            // If the page exist and has been set in the script.
            if (pageExist(request.uri())) {
            	response = MainWebServer.setHeaders(response);
                Page page = getHandler(request.uri()).newInstance();

                page.setContent(new HashMap<>());

                page.handle(request, response);

                if (page.haveNullContent()){
                    page.removeContent();
                }

                String json = MainWebServer.translateJson(page.getResponseContent().get());

                response.status(200);

                return json;

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
            answer.put("message", "The Stackit API is under maintenance, please retry later");

            String content = MainWebServer.translateJson(answer);
            response.status(200);
            return content;
        }
	}
}
