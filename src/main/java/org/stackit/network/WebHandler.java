package org.stackit.network;

import org.stackit.Logger;
import org.stackit.config.StackItConfiguration;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;

public class WebHandler {
	public static HashMap<String, Class<? extends Page>> pages = new HashMap<>();
	
	/**
	 * Get all the pages available on the webserver.
	 * @return HashMap<String, Class<? extends Page> Pages
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
	    try {
            if (!pageExist(path)) {
                pages.put(path, handler);
            }
        } catch (Exception e){
	        Logger.error("Error while loading context '" + path + "'");
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
            if (pageExist(request.uri())) {
            	response = WebServer.setHeaders(response);
                Page page = getHandler(request.uri()).newInstance();

                page.setContent(new HashMap<>());
                page.setRequest(request);
                page.setResponse(response);

                if (page instanceof Authenticate){
                    if (request.queryParams().contains("token")){
                        if (TokenManager.isTokenValid(request.queryParams("token"))){
                            page.handle();
                        } else {
                            page.status(StatusMessage.INVALID_TOKEN, StatusType.UNAUTHORIZED);
                        }
                    } else {
                        page.status(StatusMessage.BAD_REQUEST, StatusType.BAD_REQUEST);
                    }
                } else {
                    page.handle();
                }

                if (page.getAPIState().equals(StatusType.SUCCESS)){
                    Logger.warn(request.ip() + " successfully requested context " + request.uri());
                } else {
                    Logger.warn(request.ip() + " unsuccessfully requested context " + request.uri());
                }
                for (String s : request.queryParams()) {
                    Logger.warn("- " + s + " = " + request.queryParams(s));
                }

                if (page.haveNullContent()){
                    page.removeContent();
                }

                return WebServer.translateJson(page.getResponseContent());

            } else { // Page not found.

                HashMap<String, Object> answer = new HashMap<String, Object>();
            	response = WebServer.setHeaders(response);

                HashMap<String, Object> errors = new HashMap<>();
                answer.put("status", StatusType.NOT_FOUND.toString());
                errors.put("not_found", "not_found");

                answer.put("errors", errors);
                String content = WebServer.translateJson(answer);
                response.status(404);
                return content;
            }
        } else {
            HashMap<String, Object> answer = new HashMap<String, Object>();
        	response = WebServer.setHeaders(response);

            answer.put("status", StatusType.ERROR.toString());
            HashMap<String, Object> errors = new HashMap<>();
            errors.put("error", "error");

            answer.put("error", errors);

            String content = WebServer.translateJson(answer);
            response.status(500);
            return content;
        }
	}
}
