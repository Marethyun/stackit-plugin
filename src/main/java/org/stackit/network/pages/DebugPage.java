package org.stackit.network.pages;

import org.stackit.StackIt;
import spark.Request;
import spark.Response;

public class DebugPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        try {
            getContent().put("uuid", StackIt.getPlugin().getServer().getOfflinePlayer("florentlife").getUniqueId().toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        success("Hello, World");
    }
}
