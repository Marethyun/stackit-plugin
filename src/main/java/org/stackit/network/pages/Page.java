package org.stackit.network.pages;

import spark.Request;
import spark.Response;

import java.util.HashMap;

public interface Page {
	/**
	 * Return a response to the user.
	 */
	HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer, HashMap<String, Object> responseContent) throws Exception;
}
