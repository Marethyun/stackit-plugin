package org.stackit.network;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.stackit.config.StackItConfiguration;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PageNotFound implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		HashMap<String, Object> answer = new HashMap<String, Object>();
		exchange = MainWebServer.setHeaders(exchange);
		
		if(!StackItConfiguration.isInMaintenance()) {
			answer.put("error", "endpoint_not_found");
			answer.put("success", false);
		} else {
			answer.put("error", StackItConfiguration.getAPIMaintenanceError());
			answer.put("success", false);
		}
		
		JSONObject content = MainWebServer.output(answer);
		exchange.sendResponseHeaders(200, content.toJSONString().length());
		
		OutputStream os = exchange.getResponseBody();
		os.write(content.toJSONString().getBytes());
		os.close();
	}

}
