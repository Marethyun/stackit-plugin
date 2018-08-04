package org.stackit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class StackItCommand implements CommandExecutor {

    public static final String PREFIX = ChatColor.AQUA + "[StackIt] ";
    public static final String DEFAULT_MESSAGE = PREFIX +  "Hey ! The StackIt main command does nothing unless you specify some arguments..";
    public static final String PERMISSION_MISSING = PREFIX + ChatColor.RED + "You don't have the permission to perform this command...";

    private final HashMap<String, StackItCommand.Option> registeredOptions = new LinkedHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission(StackItPermissions.STACKIT_COMMAND.getPermission())){
            if (args.length > 0){
                StackItCommand.Option option = registeredOptions.get(args[1]);

                if (option == null) {
                    sender.sendMessage(DEFAULT_MESSAGE);
                    sender.sendMessage(PREFIX + "Usage: " + command.getUsage());
                } else {
                    return option.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                }
            } else {
                sender.sendMessage(DEFAULT_MESSAGE);
                sender.sendMessage(PREFIX + "Usage: " + command.getUsage());
            }
        } else {
            sender.sendMessage(PERMISSION_MISSING);
        }
        return true;
    }

    public void registerOption(StackItCommand.Option option){
        this.registeredOptions.put(option.getLabel(), option);
    }

    public abstract class Option implements CommandExecutor {
        protected String label;
        protected String description = "(No description)";
        protected String usage = "(No usage)";
        protected String permission = StackItPermissions.STACKIT_COMMAND.getPermission();

        public Option(String label, String description, String usage, String permission) {
            this.label = label;
            this.description = description;
            this.usage = usage;
            this.permission = permission;
        }

        public Option(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        public String getUsage() {
            return usage;
        }

        public String getPermission() {
            return permission;
        }
    }
}
