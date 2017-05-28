package org.stackit.network;

import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

public interface Page {
	/**
	 * Return a response to the user.
	 */
	public HashMap<String, Object> handle(HttpExchange exchange, HashMap<String, Object> answer);
}
