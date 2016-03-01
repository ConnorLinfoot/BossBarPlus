package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import com.connorlinfoot.bossbarplus.Handlers.ConfigHandler;
import com.connorlinfoot.bossbarplus.Listeners.PlayerJoin;
import com.connorlinfoot.bossbarplus.Listeners.PlayerQuit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
    private static BossBarPlus bossBarPlus;
    private static ConfigHandler configHandler = new ConfigHandler();

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

        getServer().getPluginCommand("bbp").setExecutor(new BossBarCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

    }

    public static BossBarPlus getBossBarPlus() {
        return bossBarPlus;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

}
