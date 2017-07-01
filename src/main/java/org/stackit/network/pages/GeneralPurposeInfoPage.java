package org.stackit.network.pages;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.network.StatusType;
import org.stackit.network.TokenManager;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralPurposeInfoPage implements Page {

    @Override
    public HashMap<String, Object> handle(Request request, Response response, HashMap<String, Object> answer, HashMap<String, Object> responseContent) throws Exception {
        if (request.queryParams().contains("token")){
            String token = request.queryParams("token");
            if (token != null) {
                if (TokenManager.isTokenValid(token)) {

                    Server server = StackIt.getPlugin().getServer();
                    ArrayList<String> players = new ArrayList<>();
                    for (Player player : server.getOnlinePlayers()) {
                        players.add(player.getUniqueId().toString());
                    }

                    responseContent.put("players", players);
                    responseContent.put("maxplayers", server.getMaxPlayers());
                    responseContent.put("motd", server.getMotd());
                    responseContent.put("version", server.getVersion());

                    answer.put("status", StatusType.SUCCESS);
                    answer.put("message", "Data successfully got");
                } else {
                    answer.put("status", StatusType.ERROR);
                    answer.put("message", "The provided token is invalid");
                }
            } else {
                answer.put("status", StatusType.ERROR);
                answer.put("message", "Bad request");
            }
        } else {
            answer.put("status", StatusType.ERROR);
            answer.put("message", "Bad request");
        }
        return answer;
    }
}
