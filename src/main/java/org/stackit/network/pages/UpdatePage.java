package org.stackit.network.pages;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.stackit.StackIt;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.QueueElement;
import org.stackit.network.Authenticate;
import org.stackit.network.Page;
import org.stackit.network.StatusMessage;
import org.stackit.network.StatusType;
import spark.Request;

import java.util.List;
import java.util.UUID;

public class UpdatePage extends Page implements Authenticate {

    @Override
    public void handle() {
        Request request = getRequest();
        if (request.queryParams().contains("code")) {
            String code = request.queryParams("code");
            if (code.matches("[a-z|A-Z|0-9]{8}")) {
                List<QueueElement> elements = DatabaseManager.getQueue().getAll();
                for (QueueElement element : elements) {
                    if (element.getUid().equalsIgnoreCase(code)) {
                        OfflinePlayer offlinePlayer = StackIt.getPlugin().getServer().getOfflinePlayer(UUID.fromString(element.getPlayerUuid()));
                        if (offlinePlayer.isOnline()) {

                            Player p = offlinePlayer.getPlayer();
                            p.sendMessage(StackIt.getInfoMessage("You have 1 new package !"));
                            p.sendMessage(StackIt.getInfoMessage("Type '/stackit package " + p.getDisplayName() + " " + code + "' to check it out !"));
                            p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 3.0f, 0.5f);

                            status(StatusMessage.SUCCESS, StatusType.SUCCESS);
                            return;
                        }
                        break;
                    }
                }
                status(StatusMessage.ELEMENT_NOT_FOUND, StatusType.NOT_FOUND);
            } else {
                status(StatusMessage.BAD_REQUEST, StatusType.BAD_REQUEST);
            }
        } else {
            status(StatusMessage.BAD_REQUEST, StatusType.BAD_REQUEST);
        }
    }
}
