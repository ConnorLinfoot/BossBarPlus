package com.connorlinfoot.bossbarplus;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarAPI {
    private static boolean barCurrentlyRunning = false;
    private static BossBar globalBossBar = null;
    private static String globalBossBarPerm = null;
    private static BossBar globalJoinBossBar = null;
    private static double currentTime = 0;
    private static int taskID = 0;
    private static boolean perTick = false; // If true it will run 20 times per second!

    public static void sendMessageToAllPlayersRecurring(final String message, double seconds, final BarColor barColor, final BarStyle barStyle, final String permission) {
        Bukkit.getScheduler().cancelTask(taskID);
        globalBossBarPerm = permission;
        final double perTime;
        if (perTick) {
            currentTime = seconds * 20;
            perTime = 1 / (seconds * 20);
        } else {
            currentTime = seconds;
            perTime = 1 / seconds;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentTime < 0) {
                    clearAllPlayers();
                    return;
                }
                sendMessageToAllPlayers(message, currentTime * perTime, barColor, barStyle, permission);
                currentTime--;
            }
        };

        barCurrentlyRunning = true;
        if (perTick) {
            taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(BossBarPlus.getPlugin(), runnable, 0L, 1L).getTaskId();
        } else {
            taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(BossBarPlus.getPlugin(), runnable, 0L, 20L).getTaskId();
        }
    }

    public static void sendMessageToAllPlayers(String message, double progress, BarColor barColor, BarStyle barStyle, String permission) {
        if (globalBossBar == null) {
            globalBossBar = Bukkit.createBossBar(message, barColor, barStyle);
            globalBossBar.show();
        } else {
            globalBossBar.setTitle(message);
            globalBossBar.setColor(barColor);
            globalBossBar.setStyle(barStyle);
        }

        if( globalBossBar.getPlayers().size() == 0 && Bukkit.getOnlinePlayers().size() > 0 ) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (permission != null && !permission.isEmpty() && player.hasPermission(permission))
                    continue;
                globalBossBar.addPlayer(player);
            }
        }
        globalBossBar.setProgress(progress);
    }

    public static void clearAllPlayers() {
        Bukkit.getScheduler().cancelTask(taskID);
        barCurrentlyRunning = false;
        globalBossBar.hide();
        globalBossBar.removeAll();
        globalBossBar.setTitle("");
    }

    public static void createJoinBossBar(String message, double time, BarColor barColor, BarStyle barStyle) {
        if (globalJoinBossBar == null) {
            globalJoinBossBar = Bukkit.createBossBar(message, barColor, barStyle);
        }

        if (Bukkit.getOnlinePlayers().size() > 0 && time <= 0) {
            // In case the plugin was reloaded and we should always show the bar
            for (Player player : Bukkit.getOnlinePlayers()) {
                globalJoinBossBar.addPlayer(player);
            }
        }
        globalJoinBossBar.show();
    }

    public static BossBar getGlobalJoinBossBar() {
        return globalJoinBossBar;
    }

    public static boolean isBarCurrentlyRunning() {
        return barCurrentlyRunning;
    }

    public static BossBar getGlobalBossBar() {
        return globalBossBar;
    }

    public static String getGlobalBossBarPerm() {
        return globalBossBarPerm;
    }

}
