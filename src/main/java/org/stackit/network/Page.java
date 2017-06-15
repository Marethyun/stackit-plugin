package org.stackit.network;

import spark.Request;
import spark.Response;

import java.util.HashMap;

public interface Page {
	/**
	 * Return a response to the user.
	 */
	public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer);
}
