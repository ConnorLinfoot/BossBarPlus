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
    private static BossBar joinBossBar = null;
    private static double currentTime = 0;
    private static int taskID = 0;
    private static boolean perTick = BossBarPlus.getConfigHandler().isSmooth(); // If true it will run 20 times per second!

    public static void broadcastBar(final String message, double seconds) {
        broadcastBar(message, seconds, BossBarPlus.getConfigHandler().getDefaultColor(), BossBarPlus.getConfigHandler().getDefaultStyle(), null);
    }

    public static void broadcastBar(final String message, double seconds, final BarColor barColor, final BarStyle barStyle) {
        broadcastBar(message, seconds, barColor, barStyle, null);
    }

    public static void broadcastBar(final String message, double seconds, final BarColor barColor, final BarStyle barStyle, final String permission) {
        broadcastBar(message, seconds, barColor, barStyle, permission, BossBarPlus.getConfigHandler().isSoundEnabled(), BossBarPlus.getConfigHandler().getDefaultSound());
    }

    public static void broadcastBar(final String message, double seconds, final BarColor barColor, final BarStyle barStyle, final String permission, boolean soundEnabled, Sound sound) {
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
        if (soundEnabled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, 1, 1);
            }
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentTime < 0) {
                    clearBar();
                    return;
                }
                sendBar(message, barColor, barStyle, currentTime * perTime, permission);
                currentTime--;
            }
        };

        barCurrentlyRunning = true;
        if (perTick) {
            taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(BossBarPlus.getBossBarPlus(), runnable, 0L, 1L).getTaskId();
        } else {
            taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(BossBarPlus.getBossBarPlus(), runnable, 0L, 20L).getTaskId();
        }
    }

    public static void sendBar(String message) {
        sendBar(message, BossBarPlus.getConfigHandler().getDefaultColor(), BossBarPlus.getConfigHandler().getDefaultStyle(), 1, null);
    }

    public static void sendBar(String message, BarColor barColor, BarStyle barStyle) {
        sendBar(message, barColor, barStyle, 1, null);
    }

    public static void sendBar(String message, BarColor barColor, BarStyle barStyle, double progress) {
        sendBar(message, barColor, barStyle, progress, null);
    }

    public static void sendBar(String message, BarColor barColor, BarStyle barStyle, double progress, String permission) {
        if (globalBossBar == null) {
            globalBossBar = Bukkit.createBossBar(message, barColor, barStyle);
            globalBossBar.show();
        } else {
            globalBossBar.setTitle(message);
            globalBossBar.setColor(barColor);
            globalBossBar.setStyle(barStyle);
        }

        if (globalBossBar.getPlayers().size() == 0 && Bukkit.getOnlinePlayers().size() > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (permission != null && !permission.isEmpty() && player.hasPermission(permission))
                    continue;
                globalBossBar.addPlayer(player);
            }
        }
        globalBossBar.setProgress(progress);
    }

    public static void clearBar() {
        Bukkit.getScheduler().cancelTask(taskID);
        barCurrentlyRunning = false;
        globalBossBar.hide();
        globalBossBar.removeAll();
        globalBossBar.setTitle("");
    }

    public static void setupJoinBossBar(String message, double time, BarColor barColor, BarStyle barStyle) {
        if (joinBossBar == null) {
            joinBossBar = Bukkit.createBossBar(message, barColor, barStyle);
        }

        if (Bukkit.getOnlinePlayers().size() > 0 && time <= 0) {
            // In case the plugin was reloaded and we should always show the bar
            for (Player player : Bukkit.getOnlinePlayers()) {
                joinBossBar.addPlayer(player);
            }
        }
        joinBossBar.show();
    }

    public static BossBar getJoinBossBar() {
        return joinBossBar;
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
