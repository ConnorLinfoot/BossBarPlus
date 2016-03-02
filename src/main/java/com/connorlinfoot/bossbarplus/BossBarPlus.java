package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import com.connorlinfoot.bossbarplus.Handlers.ConfigHandler;
import com.connorlinfoot.bossbarplus.Listeners.PlayerJoin;
import com.connorlinfoot.bossbarplus.Listeners.PlayerQuit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
    private static BossBarPlus bossBarPlus;
    private static ConfigHandler configHandler = new ConfigHandler();
    private int nextMessage = 0;

    public static BossBarPlus getBossBarPlus() {
        return bossBarPlus;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public void onEnable() {
        bossBarPlus = this;
        getConfig().options().copyDefaults(true);
        saveConfig();

        if (getConfig().getBoolean("Metrics")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }

        configHandler.loadConfig(getConfig(), getLogger());
        if (configHandler.isAnnouncerEnabled())
            startAnnouncerTask();

        getServer().getPluginCommand("bbp").setExecutor(new BossBarCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

    }

    @Override
    public void onDisable() {
        BossBarAPI.clearBar();
        BossBarAPI.clearJoinBar();
    }

    private void startAnnouncerTask() {
        Runnable announcerRunnable = new Runnable() {
            @Override
            public void run() {
                String message = ChatColor.translateAlternateColorCodes('&', configHandler.getAnnouncerMessages().get(nextMessage));
                BossBarAPI.sendBar(message);
                nextMessage++;
                if (nextMessage > configHandler.getAnnouncerMessages().size() - 1)
                    nextMessage = 0;
            }
        };
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, announcerRunnable, 0L, (long) (configHandler.getAnnouncerTime() * 20L));
    }

}
