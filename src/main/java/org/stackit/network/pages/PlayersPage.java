package org.stackit.network.pages;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;

import java.util.ArrayList;

public class PlayersPage extends Page implements Authenticate {

    @Override
    public void handle() {
        Server server = StackIt.getPlugin().getServer();
        ArrayList<String> players = new ArrayList<>();

        for (Player player : server.getOnlinePlayers()) {
            players.add(player.getUniqueId().toString());
        }

        getContent().put("players", players);

        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }
}
