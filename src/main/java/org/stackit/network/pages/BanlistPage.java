package org.stackit.network.pages;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.stackit.StackIt;
import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;
import spark.Request;
import spark.Response;

import java.util.ArrayList;

public class BanlistPage extends Page implements Authenticate {

    @Override
    public void handle() {
        Request request = getRequest();
        Response response = getResponse();
        Server server = StackIt.getPlugin().getServer();
        ArrayList<String> players = new ArrayList<>();

        for (OfflinePlayer player : server.getBannedPlayers()) {
            players.add(player.getUniqueId().toString());
        }

        getContent().put("banned", players);

        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }
}
