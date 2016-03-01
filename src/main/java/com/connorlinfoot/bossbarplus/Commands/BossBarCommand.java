package com.connorlinfoot.bossbarplus.Commands;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import com.connorlinfoot.bossbarplus.BossBarPlus;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossBarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && !sender.hasPermission("bossbarplus.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            return false;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown argument, use /" + label + " help, for help");
                    break;
                case "help":
                    sendHelp(sender, label);
                    break;
                case "broadcast":
                case "bc":
                    if (args.length < 3) {
                        sender.sendMessage(ChatColor.RED + "Correct Usage: /" + label + " " + args[0].toLowerCase() + " <time> <args> <message>");
                        return false;
                    }

                    int time = 10;
                    int start = 2;
                    try {
                        time = Integer.parseInt(args[1]);
                    } catch (Exception ignored) {
                        start = 1;
                    }

                    if (time < 1) {
                        sender.sendMessage(ChatColor.RED + "Time can not be less than 1!");
                        return false;
                    }

                    BarColor bossBarColor = BossBarPlus.getConfigHandler().getDefaultColor();
                    BarStyle bossBarStyle = BossBarPlus.getConfigHandler().getDefaultStyle();
                    String permission = "";

                    StringBuilder messageBuffer = new StringBuilder();
                    for (int i = start; i < args.length; i++) {
                        if (bossBarColor == null) {
                            try {
                                bossBarColor = BarColor.valueOf(args[i]);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Invalid option for -c, check the options here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BarColor.html");
                                return false;
                            }
                            continue;
                        } else if (bossBarStyle == null) {
                            try {
                                bossBarStyle = BarStyle.valueOf(args[i]);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Invalid option for -s, check the options here: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BarStyle.html");
                                return false;
                            }
                            continue;
                        } else if (permission == null) {
                            permission = args[i];
                            continue;
                        } else if (args[i].toLowerCase().equals("-c")) {
                            bossBarColor = null;
                            continue;
                        } else if (args[i].toLowerCase().equals("-s")) {
                            bossBarStyle = null;
                            continue;
                        } else if (args[i].toLowerCase().equals("-p")) {
                            permission = null;
                            continue;
                        }
                        messageBuffer.append(' ').append(args[i]);
                    }
                    String message = ChatColor.translateAlternateColorCodes('&', messageBuffer.toString());

                    if (message.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "Please enter a message!");
                        return false;
                    }

                    BossBarAPI.broadcastBar(message, time, bossBarColor, bossBarStyle, permission);

                    sender.sendMessage(ChatColor.GREEN + "Broadcast has been sent successfully");
                    break;
                case "clear":
                    BossBarAPI.clearBar();
                    sender.sendMessage(ChatColor.GREEN + "Cleared BossBar");
                    break;

            }
        } else {
            sender.sendMessage(ChatColor.AQUA + "BossBarPlus v" + BossBarPlus.getBossBarPlus().getDescription().getVersion() + " - Created by Connor Linfoot");
        }

        return false;
    }

    private void sendHelp(CommandSender sender, String label){
        sender.sendMessage(ChatColor.YELLOW + "---------- " + ChatColor.AQUA + ChatColor.BOLD + "BossBarPlus" + ChatColor.YELLOW + " ----------");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " broadcast <time> <args> <message>" + ChatColor.AQUA + " - Broadcast a message to all players with the boss bar");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " clear" + ChatColor.AQUA + " - Clear boss bar (if there is one)");
    }

}
