package org.stackit.network.pages;

import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

public class DebugPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        setAPIState(StatusType.SUCCESS);
        setMessage("Hello, World");
    }
}
