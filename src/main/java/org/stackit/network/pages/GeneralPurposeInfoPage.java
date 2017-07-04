package org.stackit.network.pages;

import org.bukkit.Server;
import org.stackit.StackIt;
import org.stackit.network.TokenManager;
import spark.Request;
import spark.Response;

public class GeneralPurposeInfoPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.queryParams().contains("token")){
            String token = request.queryParams("token");
            if (TokenManager.isTokenValid(token)) {

                Server server = StackIt.getPlugin().getServer();

                getContent().put("maxplayers", server.getMaxPlayers());
                getContent().put("motd", server.getMotd());
                getContent().put("version", server.getVersion());

                success("Data successfully got");

            } else {
                response.status(401);
                error("Provided token is invalid");
            }
        } else {
            response.status(400);
            error("Bad request");
        }
    }
}
