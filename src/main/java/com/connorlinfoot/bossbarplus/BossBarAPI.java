package com.connorlinfoot.bossbarplus;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarAPI {
    private static BossBar globalBossBar = null;
    private static double currentSecond = 0;
    private static int taskID = 0;

    public static void sendMessageToAllPlayersRecuring(final String message, double seconds, final BarColor barColor, final BarStyle barStyle) {
        final double perSecond = 1 / seconds;
        Bukkit.getScheduler().cancelTask(taskID);
        currentSecond = seconds;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentSecond < 0) {
                    clearAllPlayers();
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
                sendMessageToAllPlayers(message, currentSecond * perSecond, barColor, barStyle);
                currentSecond--;
            }
        };
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(BossBarPlus.getPlugin(), runnable, 0L, 20L).getTaskId();
    }

    public static void sendMessageToAllPlayers(String message, double progress, BarColor barColor, BarStyle barStyle) {
        if (globalBossBar == null) {
            globalBossBar = Bukkit.createBossBar(message, barColor, barStyle);
        } else {
            globalBossBar.setTitle(message);
            globalBossBar.setColor(barColor);
            globalBossBar.setStyle(barStyle);
        }

        globalBossBar.removeAll();
        for (Player player : Bukkit.getOnlinePlayers()) {
            globalBossBar.addPlayer(player);
        }
        globalBossBar.setProgress(progress);
        globalBossBar.show();
    }

    public static void clearAllPlayers() {
        Bukkit.getScheduler().cancelTask(taskID);
        globalBossBar.hide();
        globalBossBar.removeAll();
        globalBossBar.setTitle("");
    }

}
