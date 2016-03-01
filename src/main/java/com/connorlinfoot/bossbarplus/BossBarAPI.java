package com.connorlinfoot.bossbarplus;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarAPI {
    private static BossBar globalBossBar = null;

    public static void sendMessageToAllPlayers(final String message, int seconds, final BarColor barColor, final BarStyle barStyle) {
        final double perSecond = 1 / seconds;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sendMessageToAllPlayers(message, .5, barColor, barStyle);
            }
        };
        runnable.run();
    }

    public static void sendMessageToAllPlayers(String message, double progress) {
        sendMessageToAllPlayers(message, progress, BarColor.GREEN, BarStyle.SEGMENTED_6);
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

}
