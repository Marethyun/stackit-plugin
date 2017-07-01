package org.stackit.network.pages;

import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class DebugPage implements Page {

    @Override
    public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer, HashMap<String, Object> responseContent) throws Exception {
        answer.put("status", StatusType.SUCCESS);
        answer.put("message", "Hello, World");
        return answer;
    }
}
