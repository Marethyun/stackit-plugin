package org.stackit.network.pages;

import spark.Request;
import spark.Response;

public class DebugPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        success("Hello, World");
    }
}
