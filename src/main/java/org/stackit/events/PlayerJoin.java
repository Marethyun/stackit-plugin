package org.stackit.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.QueueElement;

import java.util.List;

public class PlayerJoin implements Listener {

    private Player sender;

    private String prefix = ChatColor.AQUA + "[StackIt] ";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        this.sender = e.getPlayer();
        String uuid = this.sender.getUniqueId().toString();

        List<QueueElement> elements = DatabaseManager.getQueue().getByUserUuid(uuid);

        if (!elements.isEmpty()){
            sendInfoHeader("You have " + elements.size() + " active package" + (elements.size() == 1 ? "" : "s"));
            sendInfoHeader("Type '/stackit packages' to check " + (elements.size() == 1 ? "it" : "these") + " out !");
        }

    }

    private void sendInfoHeader(String message){
        this.sender.sendMessage(prefix + ChatColor.AQUA  + message);
    }

}
