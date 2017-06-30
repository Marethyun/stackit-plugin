package org.stackit.network;

import spark.Request;
import spark.Response;

import java.util.HashMap;

public class PageDebug implements Page {

    @Override
    public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer) {
        answer.put("message", "Hello, World");
        return answer;
    }
}
