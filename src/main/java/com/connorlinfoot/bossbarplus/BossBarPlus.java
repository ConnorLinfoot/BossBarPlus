package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
    private static BarColor barColor = BarColor.PURPLE;
    private static BarStyle barStyle = BarStyle.SOLID;

    public void onEnable() {
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

        getServer().getPluginCommand("bbp").setExecutor(new BossBarCommand());
        barColor = BarColor.valueOf(getConfig().getString("Boss Bar Color").toUpperCase());
        barStyle = BarStyle.valueOf(getConfig().getString("Boss Bar Style").toUpperCase());

    }

    public static BarColor getBarColor() {
        return barColor;
    }

    public static BarStyle getBarStyle() {
        return barStyle;
    }

}
