package com.connorlinfoot.bossbarplus.Commands;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import com.connorlinfoot.bossbarplus.BossBarPlus;
import org.bukkit.Bukkit;
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
                    sender.sendMessage(ChatColor.RED + "Unknown argument");
                    break;
                case "help":
                    // TODO; Do help
                    sender.sendMessage(ChatColor.YELLOW + "TODO");
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

                    BarColor bossBarColor = BossBarPlus.getBarColor();
                    BarStyle bossBarStyle = BossBarPlus.getBarStyle();

                    StringBuilder messageBuffer = new StringBuilder();
                    for (int i = start; i < args.length; i++) {
                        if (bossBarColor == null) {
                            bossBarColor = BarColor.valueOf(args[i]);
                            continue;
                        } else if (bossBarStyle == null) {
                            bossBarStyle = BarStyle.valueOf(args[i]);
                            continue;
                        } else if (args[i].toLowerCase().equals("-c")) {
                            bossBarColor = null;
                            continue;
                        } else if (args[i].toLowerCase().equals("-s")) {
                            bossBarStyle = null;
                            continue;
                        }
                        messageBuffer.append(' ').append(args[i]);
                    }
                    String message = ChatColor.translateAlternateColorCodes('&', messageBuffer.toString());

                    if (message.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "Please enter a message!");
                        return false;
                    }

                    BossBarAPI.sendMessageToAllPlayersRecuring(message, time, bossBarColor, bossBarStyle);

                    sender.sendMessage(ChatColor.GREEN + "Broadcast successful");
                    break;
                case "clear":
                    // TODO; Clear boss bar
                    break;

            }
        }

        return false;
    }

}
