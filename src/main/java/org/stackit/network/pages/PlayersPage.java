package org.stackit.network.pages;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.network.TokenManager;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class PlayersPage extends Page {

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.queryParams().contains("token")){
            if (TokenManager.isTokenValid(request.queryParams("token"))){
                Server server = StackIt.getPlugin().getServer();
                ArrayList<String> players = new ArrayList<>();
                for (Player player : server.getOnlinePlayers()) {
                    players.add(player.getUniqueId().toString());
                }

                getContent().put("players", players);

                success("Data successfully got");
            } else {
                error("The provided token is invalid");
            }
        } else {
            error("Bad request");
        }
    }
}
