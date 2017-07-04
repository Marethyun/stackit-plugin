package org.stackit.network.pages;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.stackit.StackIt;
import org.stackit.network.TokenManager;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class BanlistPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {

        if (request.queryParams().contains("token")){
            if (TokenManager.isTokenValid(request.queryParams("token"))){
                Server server = StackIt.getPlugin().getServer();
                ArrayList<String> players = new ArrayList<>();

                for (OfflinePlayer player : server.getBannedPlayers()) {
                    players.add(player.getUniqueId().toString());
                }

                getContent().put("banned", players);

                success("Data successfully got");
            } else {
                response.status(401);
                error("The provided token is invalid");
            }
        } else {
            response.status(400);
            error("Bad request");
        }
    }
}
