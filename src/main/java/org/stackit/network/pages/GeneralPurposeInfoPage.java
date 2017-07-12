package org.stackit.network.pages;

import org.bukkit.Server;
import org.stackit.StackIt;
import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;

public class GeneralPurposeInfoPage extends Page implements Authenticate {

    @Override
    public void handle() {

        Server server = StackIt.getPlugin().getServer();

        getContent().put("maxplayers", server.getMaxPlayers());
        getContent().put("motd", server.getMotd());
        getContent().put("version", server.getVersion());

        status(StatusMessage.SUCCESS, StatusType.SUCCESS);
    }
}
