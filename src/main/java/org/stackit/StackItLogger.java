package org.stackit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.stackit.StackItLogger.LevelColors.*;

public final class StackItLogger {

    private final CommandSender sender;
    private final String prefix;

    public StackItLogger(CommandSender sender, String prefix) {
        this.sender = sender;
        this.prefix = prefix;
    }

    public StackItLogger(CommandSender sender) {
        this(sender, ChatColor.AQUA + "[StackIt] ");
    }

    public void info(Object message){
        sender.sendMessage(this.prefix + INFO.color + message);
    }

    public void warn(Object message){
        sender.sendMessage(this.prefix + WARN.color + message);
    }

    public void debug(Object message){
        sender.sendMessage(this.prefix + DEBUG.color + message);
    }

    public void error(Object message){
        sender.sendMessage(this.prefix + ERROR.color + message);
    }

    public void fatal(Object message){
        sender.sendMessage(this.prefix + FATAL.color + message);
    }

    public void success(Object message){
        sender.sendMessage(this.prefix + SUCCESS.color + message);
    }

    public enum LevelColors {
        INFO(ChatColor.AQUA),
        WARN(ChatColor.YELLOW),
        DEBUG(ChatColor.DARK_AQUA),
        ERROR(ChatColor.RED),
        FATAL(ChatColor.DARK_RED),
        SUCCESS(ChatColor.GREEN);

        public ChatColor color;

        LevelColors(ChatColor color) {
            this.color = color;
        }
    }
}
