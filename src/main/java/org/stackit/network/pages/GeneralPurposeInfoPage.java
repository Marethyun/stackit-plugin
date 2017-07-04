package org.stackit.network.pages;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.network.TokenManager;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class GeneralPurposeInfoPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.queryParams().contains("token")){
            String token = request.queryParams("token");
            if (TokenManager.isTokenValid(token)) {

                Server server = StackIt.getPlugin().getServer();
                ArrayList<String> players = new ArrayList<>();
                for (Player player : server.getOnlinePlayers()) {
                    players.add(player.getUniqueId().toString());
                }

                getContent().put("players", players);
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