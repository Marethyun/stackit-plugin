package org.stackit.network.pages;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.stackit.StackIt;
import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;

import java.util.ArrayList;

public class WhitelistPage extends Page implements Authenticate {

    @Override
    public void handle() {
        Server server = StackIt.getPlugin().getServer();
        ArrayList<String> players = new ArrayList<>();

        for (OfflinePlayer player : server.getWhitelistedPlayers()) {
            players.add(player.getUniqueId().toString());
        }

        getContent().put("players", players);
        getContent().put("activated", server.hasWhitelist());

        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }
}
