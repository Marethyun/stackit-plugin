package org.stackit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class StackItCommand extends StackItContainer implements CommandExecutor {

    private StackItLogger logger;

    public static final String DEFAULT_MESSAGE = "Hey ! The StackIt main command does nothing unless you specify some arguments..";
    public static final String PERMISSION_MISSING = "You don't have the permission to perform this command (%s)";

    private final HashMap<String, StackItCommand.Option> registeredOptions = new LinkedHashMap<>();

    public StackItCommand(StackIt pluginInstance) {
        super(pluginInstance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        this.logger = new StackItLogger(sender);

        if (sender.hasPermission(StackItPermissions.STACKIT_COMMAND.getPermission())){
            if (args.length > 0){
                StackItCommand.Option option = registeredOptions.get(args[0]);

                if (option == null) {
                    logger.info(DEFAULT_MESSAGE);
                    logger.info("Usage: " + command.getUsage());
                } else {
                    if (!sender.hasPermission(option.getPermission())){
                        logger.error(String.format(PERMISSION_MISSING, option.getPermission()));
                    } else {
                        return option.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    }
                }
            } else {
                logger.info(DEFAULT_MESSAGE);
                logger.info("Usage: " + command.getUsage());
            }
        } else {
            logger.error(String.format(PERMISSION_MISSING, StackItPermissions.STACKIT_COMMAND.getPermission()));
        }
        return true;
    }

    public void registerOption(StackItCommand.Option option){
        this.registeredOptions.put(option.getLabel(), option);
    }

    public static abstract class Option implements CommandExecutor {
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
