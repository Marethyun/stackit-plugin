package org.stackit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.stackit.StackIt;
import org.stackit.database.DatabaseManager;
import org.stackit.database.entities.QueueElement;

import java.util.List;
import java.util.UUID;

public class MainCommand implements CommandExecutor{

    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;
    private Server server;
    private String prefix = ChatColor.AQUA + "[StackIt] ";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        this.sender = commandSender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.server = StackIt.getPlugin().getServer();
        if (args.length != 0) {
            args[0] = args[0].toLowerCase();
            switch (args[0]) {
                case "claim":
                    // /stackit claim <pkg uid>
                    if (this.sender instanceof ConsoleCommandSender) {
                        sendError("The 'claim' command cannot be executed from console");
                    } else {
                        if (checkPerms("stackit.claim.use")) {
                            if (args.length == 2) {
                                if (checkPackageCode(args[1])) {
                                    claim(args[1]);
                                } else {
                                    sendError("Invalid package code, a code is looking like 'A5Z9H7EY' (case non-sensitive)");
                                }
                            } else {
                                sendError("Wrong usage, type '/stackit help claim' for more info");
                            }
                        }
                    }
                    break;

                case "package":
                    // /stackit package <pseudonym> <pkg code>
                    if (checkPerms("stackit.package.use")) {
                        if (args.length == 3) {
                            try {
                                if (!this.sender.getName().equals(args[1])) {
                                    if (checkPerms("stackit.package.others")) {
                                        _package(args[1], args[2]);
                                    }
                                } else {
                                    _package(args[1], args[2]);
                                }
                            } catch (NumberFormatException e) {
                                sendError("The page index must be a number.");
                            }
                        } else {
                            sendError("Wrong usage, type '/stackit help package' for more info");
                        }
                    }
                    break;

                case "packages":
                    // /stackit packages [pseudonym]
                    if (checkPerms("stackit.packages.use")){
                        if (args.length == 1){
                            if (this.sender instanceof Player) {
                                packages(this.sender.getName());
                            } else {
                                sendError("This command cannot be executed from console like that");
                                sendError("Type '/stackit help packages' for more info");
                            }
                        } else if (args.length == 2){
                            if (!this.sender.getName().equals(args[1])){
                                if (checkPerms("stackit.packages.others")){
                                    packages(args[1]);
                                }
                            }
                        } else {
                            sendError("Wrong usage, type '/stackit help packages' for more info");
                        }
                    }
                    break;
                case "help":
                    if (args.length == 1){
                        help();
                    } else if (args.length == 2){
                        help(args[1]);
                    }
                    break;
                default:
                    sendError("Command usage: " + this.command.getUsage());

            }
        } else {
            sendError("Command usage: " + this.command.getUsage());
        }

        return true;
    }

    private boolean checkPackageCode(String code){
        return code.matches("[a-z|A-Z|0-9]{8}");
    }

    private boolean checkPerms(String permission){
        if (this.sender instanceof ConsoleCommandSender){
            return true;
        } else if (this.sender instanceof Player){
            Player p = (Player) this.sender;
            if (p.hasPermission(permission)){
                return true;
            } else {
                sendError("You must have permission " + permission + " to perform this command");
                return false;
            }
        } else {
            return false;
        }
    }

    private void sendError(String message){
        this.sender.sendMessage(prefix + ChatColor.RED + message);
    }

    private void sendInfoHeader(String message){
        this.sender.sendMessage(prefix + ChatColor.AQUA  + message);
    }

    private void sendInfo(String message){
        this.sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.AQUA + "| " + message);
    }

    private void sendInfo(String message, ChatColor color){
        this.sender.sendMessage(ChatColor.BOLD + color.toString() + "| " + ChatColor.AQUA + message);
    }

    private void claim(String uid){
        Player p = (Player) this.sender;
        UUID uuid = p.getUniqueId();
        List<QueueElement> queueElements = DatabaseManager.getQueue().getByUserUuid(uuid.toString());
        for (QueueElement element : queueElements) {
            if (element.getUid().equalsIgnoreCase(uid)) {
                Inventory inventory = p.getInventory();
                if (freeInventorySlots(inventory) >= element.getSlotNumber()){
                    String[] commandList = parseCommands(element.getCommands());
                    try {
                        for (String command : commandList){
                            this.server.dispatchCommand(this.server.getConsoleSender(), command);
                            p.getWorld().playSound(p.getLocation(), Sound.CHEST_OPEN, 3.0f, 0.5f);
                        }

                        DatabaseManager.getQueue().delete(uid);
                        sendInfoHeader("Package successfully claimed !");
                    } catch (Exception e){
                        sendError("An error occured while receiving package..");
                    }
                } else {
                    sendError("You don't have enough space in your inventory to receive this package.");
                }
                break;
            }
        }
    }

    private int freeInventorySlots(Inventory inventory){
        ItemStack[] stacks = inventory.getContents();
        int slotNumber = 0;
        for (ItemStack stack : stacks){
            if (stack == null){
                slotNumber++;
            }
        }
        return slotNumber;
    }

    private void _package(String playerName, String code){
        UUID uuid = StackIt.UUIDbyPlayerName(playerName);
        sendInfoHeader("Database > Retrieving package...");
        List<QueueElement> queueElements = DatabaseManager.getQueue().getByUserUuid(uuid.toString());
        if (!queueElements.isEmpty()) {
            for (QueueElement element : queueElements) {
                if (element.getUid().equalsIgnoreCase(code)) {
                    sendInfoHeader(playerName + "'s package with code: " + code);
                    sendInfo(ChatColor.AQUA + "NAME: " + element.getName());
                    sendInfo(ChatColor.AQUA + "SLOTS REQUIRED: " + element.getSlotNumber());
                    String commands = element.getCommands();
                    sendInfo(ChatColor.AQUA + "COMMANDS:");
                    if (commands.contains(";")) {
                        String[] formatedCommands = commands.split(";");
                        for (String s : formatedCommands) {
                            sendInfo(ChatColor.AQUA + "- /" + s);
                        }
                    } else {
                        sendInfo(ChatColor.AQUA + "- /" + commands);
                    }
                    break;
                }
            }
        } else {
            sendError(playerName + "'s package with code " + code + " not found");
        }
    }

    private void packages(String playerName){
        UUID uuid = StackIt.UUIDbyPlayerName(playerName);
        sendInfoHeader("Database > Retrieving packages...");
        List<QueueElement> queueElements = DatabaseManager.getQueue().getByUserUuid(uuid.toString());
        if (!queueElements.isEmpty()){
            sendInfoHeader(playerName + "'s packages:");
            for (QueueElement element : queueElements) {
                sendInfo(element.getUid() + " - With name: '" + element.getName() + "'");
            }
        } else {
            if (this.sender.getName().equals(playerName)){
                sendError("You have no active packages yet");
            } else {
                sendError("The player " + playerName + " have no active packages yet");
            }
        }
    }

    private String[] parseCommands(String commandList){
        if (commandList.contains(";")){
            return commandList.split(";");
        } else {
            return new String[]{commandList};
        }
    }

    private void help(String option){
        option = option.toLowerCase();
        sendInfoHeader("===== StackIt help: '" + option + "' =====");
        switch (option){
            case "help":
                sendInfo("'/stackit help [option]'");
                sendInfo("-> Display generic help");
                sendInfo("-> Display specific help when 'option' parameter is provided");
                sendInfo("-> The parameter 'option' is optional");
                break;

            case "claim":
                sendInfo("'/stackit claim <code>'");
                sendInfo("-> Claim a StackIt package");
                sendInfo("-> The 'code' parameter MUST be provided");
                sendInfo("Permissions:");
                sendInfo("-> stackit.claim.use : command usage");
                break;

            case "package":
                sendInfo("'/stackit package <pseudonym> <code>'");
                sendInfo("-> Display information about a package");
                sendInfo("-> Parameters 'pseudonym' and 'code' MUST be provided");
                sendInfo("Permissions:");
                sendInfo("-> stackit.package.use : command usage");
                sendInfo("-> stackit.package.others : command usage on others players");
                break;

            case "packages":
                sendInfo("'/stackit packages [pseudonym]'");
                sendInfo("-> Display all packages of a player");
                sendInfo("-> The 'pseudonym' parameter is optional");
                sendInfo("Permissions:");
                sendInfo("-> stackit.packages.use");
                sendInfo("-> stackit.packages.others");
                break;

            case "options":
                sendInfo("StackIt's command option list:");
                sendInfo("-> 'help' (/stackit help help)");
                sendInfo("-> 'claim' (/stackit help claim)");
                sendInfo("-> 'package' (/stackit help package)");
                sendInfo("-> 'packages' (/stackit help packages)");
                sendInfo("-> 'options' (/stackit help options)");
                break;

            default:
                sendInfo("'" + option + "' option not found.");
                sendInfo("Type '/stackit help options for an exhaustive options list.");
        }
    }

    private void help(){
        sendInfoHeader("===== StackIt help: =====");
        sendInfo("Command usage: /stackit <option> [argument]");
        sendInfo("Or with alias 'st': /st <option> [argument]");
        sendInfo("Type '/stackit help options' for a exhaustive options list.");
    }
}
