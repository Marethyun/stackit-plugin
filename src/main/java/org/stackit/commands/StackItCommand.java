package org.stackit.commands;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.stackit.config.StackItConfiguration;
import org.stackit.database.Database;
import org.stackit.src.Language;
import org.stackit.src.StackIt;

import net.md_5.bungee.api.ChatColor;

public class StackItCommand implements CommandExecutor {
	ArrayList<Player> disableWaiting = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("stackit") || label.equalsIgnoreCase("si") || label.equalsIgnoreCase("stack")) {
			if(!StackItConfiguration.isInMaintenance()) {
				if(args.length > 0) {
					// If the subcommand is help
					if(args[0].equalsIgnoreCase("help")) {
						// If the sender is a player
						if(sender instanceof Player) {
							Player player = (Player) sender;
							String playerLanguage = Language.getPlayerLanguage(player);
							
							if(player.hasPermission("stackit.help") || player.isOp()) {
								sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + StringUtils.center("|[ " + ChatColor.AQUA + Language.get(playerLanguage, "help") + ChatColor.DARK_AQUA + ChatColor.BOLD + " ]|", 55, '-'));
								
								if(player.hasPermission("stackit.api.createuser") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_api_createuser"));
								}
								
								if(player.hasPermission("stackit.api.deleteuser") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_api_deleteuser"));
								}
								
								if(player.hasPermission("stackit.api.disable") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_disable"));
								}
								
								if(player.hasPermission("stackit.api.log") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_log"));
								}
								
								if(player.hasPermission("stackit.api.maintenance.enable") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_maintenance"));
								}
								
								if(player.hasPermission("stackit.api.internationalization.setforced") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_internationalization_setforced"));
								}
								
								if(player.hasPermission("stackit.api.internationalization.setforcedlanguage") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_internationalization_setforcedlanguage"));
								}
								
								if(player.hasPermission("stackit.api.internationalization.setdefaultlanguage") || player.isOp()) {
									sender.sendMessage(Language.get(playerLanguage, "command_internationalization_setdefaultlanguage"));
								}
							} else {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
							}
						}
						
						if(sender instanceof ConsoleCommandSender) {
							String playerLanguage = Language.getBukkitLanguage();
							
							sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + StringUtils.center("|[ " + ChatColor.AQUA + Language.get(playerLanguage, "help") + ChatColor.DARK_AQUA + ChatColor.BOLD + " ]|", 55, '-'));
							sender.sendMessage(Language.get(playerLanguage, "command_api_createuser"));
						}
					} else if(args[0].equalsIgnoreCase("createuser")) { // If subcommand is createuser
						String result = null;
						String playerLanguage = Language.getBukkitLanguage();
						
						// If the sender is a player
						if(sender instanceof Player) {
							Player player = (Player) sender;
							playerLanguage = Language.getPlayerLanguage(player);
							
							if(!player.hasPermission("stackit.api.createuser") && !player.isOp()) {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
								return false;
							}
						}
						
						String user = RandomStringUtils.randomAlphabetic(6), pass = RandomStringUtils.randomAlphanumeric(12);
						
						if(args.length >= 2) { // If at least 1 argument is defined
							if(args.length == 2) { // If only the username argument is defined
								user = args[1];
							} else { // If the username argument and the password argument are defined
								user = args[1];
								pass = args[2];
							}
						}
						
						result = Database.getActiveDatabase().CreateUser(user, pass);
						
						if(result == "success") { // If the user has been created
							sender.sendMessage(ChatColor.DARK_AQUA + "╔" + StringUtils.center(ChatColor.BOLD + "|[ " + ChatColor.AQUA + Language.get(playerLanguage, "congrats") + ChatColor.DARK_AQUA + ChatColor.BOLD + " ]|" + ChatColor.DARK_AQUA, 54, '═'));
							sender.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.GREEN + Language.get(playerLanguage, "api_user_created"));
							sender.sendMessage(ChatColor.DARK_AQUA + "║ ");
							sender.sendMessage(ChatColor.DARK_AQUA + "║ " + ChatColor.DARK_AQUA + Language.get(playerLanguage, "credentials"));
							sender.sendMessage(ChatColor.DARK_AQUA + "║   " + ChatColor.DARK_AQUA + Language.get(playerLanguage, "username") + " " + ChatColor.AQUA + user);
							sender.sendMessage(ChatColor.DARK_AQUA + "║   " + ChatColor.DARK_AQUA + Language.get(playerLanguage, "password") + " " + ChatColor.AQUA + pass);
							sender.sendMessage(ChatColor.DARK_AQUA + "╚" + StringUtils.center("", 34, '═'));
						} else if(result == "already_exist") { // If the user already exist
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(playerLanguage, "api_user_exist")));
						} else { // If an internal server error occured
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(playerLanguage, "internal_server_error")));
						}
					} else if(args[0].equalsIgnoreCase("deleteuser")) { // If subcommand is deleteuser
						String result = null;
						String playerLanguage = Language.getBukkitLanguage();
						
						// If the sender is a player
						if(sender instanceof Player) {
							Player player = (Player) sender;
							playerLanguage = Language.getPlayerLanguage(player);
							
							if(!player.hasPermission("stackit.api.deleteuser") && !player.isOp()) {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
								return false;
							}
						}
						
						if(args.length < 2) { // If there is not enough arguments
							sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
						}
						
						result = Database.getActiveDatabase().DeleteUser(args[1]);
						
						if(result == "success") { // If the user has been deleted
							sender.sendMessage(Language.get(playerLanguage, "api_user_deleted"));
						} else if(result == "not_found") { // If the user has not been found
							sender.sendMessage(Language.get(playerLanguage, "api_user_not_found"));
						} else { // If an internal server error occured
							sender.sendMessage(Language.get(playerLanguage, "internal_server_error"));
						}
					} else if(args[0].equalsIgnoreCase("disable")) { // If subcommad is disable
						if(sender instanceof Player) {
							Player player = (Player) sender;
							String playerLanguage = Language.getPlayerLanguage(player);
							
							if(player.hasPermission("stackit.disable") || player.isOp()) {
								if(args.length >= 2) {
									if(args[1].equalsIgnoreCase("confirm")) {
										if(disableWaiting.contains(player)) {
											disableWaiting.remove(player);
											
											StackItConfiguration.setEnabled(false);
											StackIt.disable();
											
											sender.sendMessage(Language.get(Language.getBukkitLanguage(), "plugin_disabled_instructions"));
											return false;
										} else {
											sender.sendMessage(Language.get(Language.getBukkitLanguage(), "plugin_disabled_request_expired"));
										}
									}
								}

								disableWaiting.add(player);
								sender.sendMessage(Language.get(Language.getBukkitLanguage(), "command_confirm_disable"));
								
								StackIt.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(StackIt.getPlugin(), new Runnable() {
									  public void run() {
										  disableWaiting.remove(player);
									  }
								}, 600L);
							} else {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
								return false;
							}
						}
						
						if(sender instanceof ConsoleCommandSender) {
							StackItConfiguration.setEnabled(false);
							StackIt.disable();
							
							sender.sendMessage(Language.get(Language.getBukkitLanguage(), "plugin_disabled_instructions"));
						}
					} else if(args[0].equalsIgnoreCase("log")) { // If subcommand is log
						String playerLanguage = null;
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							playerLanguage = Language.getPlayerLanguage(player);
							
							if(!player.hasPermission("stackit.log") && !player.isOp()) {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
							}
						} else {
							playerLanguage = Language.getBukkitLanguage();
						}
						
						if(args.length >= 2) {
							if(args[1].equalsIgnoreCase("true")) {
								StackItConfiguration.setLogEnabled(true);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else if(args[1].equalsIgnoreCase("false")) {
								StackItConfiguration.setLogEnabled(false);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else {
								sender.sendMessage(Language.get(playerLanguage, "command_invalid_arguments"));
							}
						} else {
							sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
						}
					} else if(args[0].equalsIgnoreCase("maintenance")) {
						String playerLanguage = null;
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							playerLanguage = Language.getPlayerLanguage(player);
							
							if(!player.hasPermission("stackit.log") && !player.isOp()) {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
							}
						} else {
							playerLanguage = Language.getBukkitLanguage();
						}
						
						if(args.length >= 2) {
							if(args[1].equalsIgnoreCase("true")) {
								StackItConfiguration.setInMaintenance(true);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else if(args[1].equalsIgnoreCase("false")) {
								StackItConfiguration.setInMaintenance(false);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else {
								sender.sendMessage(Language.get(playerLanguage, "command_invalid_arguments"));
							}
						} else {
							sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
						}
					} else if(args[0].equalsIgnoreCase("internationalization")) {
						String playerLanguage = null;
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							playerLanguage = Language.getPlayerLanguage(player);
						} else {
							playerLanguage = Language.getBukkitLanguage();
						}
						
						if(args.length >= 2) {
							if(args[1].equalsIgnoreCase("setforced")) {
								if(args.length >= 3) {
									if(args[2].equalsIgnoreCase("true")) {
										StackItConfiguration.setLanguageForced(true);
										sender.sendMessage(Language.get(playerLanguage, "command_success"));
									} else if(args[2].equalsIgnoreCase("false")) {
										StackItConfiguration.setLanguageForced(false);
										sender.sendMessage(Language.get(playerLanguage, "command_success"));
									} else {
										sender.sendMessage(Language.get(playerLanguage, "command_invalid_arguments"));
									}
								} else {
									sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
								}
							} else if(args[1].equalsIgnoreCase("setforcedlanguage")) {
								if(args.length >= 3) {
									StackItConfiguration.setForcedLanguage(args[2]);
									sender.sendMessage(Language.get(playerLanguage, "command_success"));
								} else {
									sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
								}
							} else if(args[1].equalsIgnoreCase("setdefaultlanguage")) {
								if(args.length >= 3) {
									StackItConfiguration.setDefaultLanguage(args[2]);
									sender.sendMessage(Language.get(playerLanguage, "command_success"));
								} else {
									sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
								}
							} else {
								sender.sendMessage(Language.get(Language.getBukkitLanguage(), "command_not_found"));
							}
						} else {
							sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
						}
					} else { // If subcommand doesn't have a context
						if(sender instanceof Player) {
							Player player = (Player) sender;
							String playerLanguage = Language.getPlayerLanguage(player);
							
							sender.sendMessage(Language.get(playerLanguage, "command_not_found"));
						} else {
							sender.sendMessage(Language.get(Language.getBukkitLanguage(), "command_not_found"));
						}
						
						return false;
					}
				} else {
					String playerLanguage = null;
					
					if(sender instanceof Player) {
						Player player = (Player) sender;
						playerLanguage = Language.getPlayerLanguage(player);
					} else {
						playerLanguage = Language.getBukkitLanguage();
					}

					sender.sendMessage(ChatColor.DARK_AQUA + "╔" + StringUtils.center(ChatColor.BOLD + "|[ " + ChatColor.AQUA + "StackIt" + ChatColor.DARK_AQUA + ChatColor.BOLD + " ]|" + ChatColor.DARK_AQUA, 50, '═'));
					sender.sendMessage(ChatColor.DARK_AQUA + "║ " + Language.get(playerLanguage, "stackit_presentation"));
					sender.sendMessage(ChatColor.DARK_AQUA + "║ " + Language.get(playerLanguage, "stackit_presentation_2"));
					sender.sendMessage(ChatColor.DARK_AQUA + "╚" + StringUtils.center("", 34, '═'));
					
					return false;
				}
			} else {
				String playerLanguage = null;
				
				// Detect sender language
				if(sender instanceof Player) { // If sender is a player
					Player player = (Player) sender;
					playerLanguage = Language.getPlayerLanguage(player);
				} else { // If sender is the console (or anything else)
					playerLanguage = Language.getBukkitLanguage();
				}
				
				if(args.length >= 1) { // ONLY WAY TO RE-ENABLE THE PLUGIN DIRECTLY IN THE GAME
					if(args[0].equalsIgnoreCase("maintenance")) {
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							
							if(!player.hasPermission("stackit.log") && !player.isOp()) {
								sender.sendMessage(Language.get(playerLanguage, "command_not_enough_permissions"));
							}
						}
						
						if(args.length >= 2) {
							if(args[1].equalsIgnoreCase("true")) {
								StackItConfiguration.setInMaintenance(true);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else if(args[1].equalsIgnoreCase("false")) {
								StackItConfiguration.setInMaintenance(false);
								sender.sendMessage(Language.get(playerLanguage, "command_success"));
							} else {
								sender.sendMessage(Language.get(playerLanguage, "command_invalid_arguments"));
							}
						} else {
							sender.sendMessage(Language.get(playerLanguage, "command_not_enough_arguments"));
						}
						
						return false;
					}
				}
				
				sender.sendMessage(StackItConfiguration.getInGameMaintenanceError(playerLanguage));
			}
		}
		
		return false;
	}

}
