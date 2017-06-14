package org.stackit.network;

import java.util.HashMap;

import spark.Request;
import spark.Response;

public interface Page {
	/**
	 * Return a response to the user.
	 */
	public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer);
}
