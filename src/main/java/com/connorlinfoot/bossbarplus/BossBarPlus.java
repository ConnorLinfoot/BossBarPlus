package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
    private static BossBarPlus bossBarPlus;
    private static BarColor barColor = BarColor.PURPLE;
    private static BarStyle barStyle = BarStyle.SOLID;

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

        getServer().getPluginCommand("bbp").setExecutor(new BossBarCommand());
        try {
            barColor = BarColor.valueOf(getConfig().getString("Default Options.Boss Bar Color").toUpperCase());
        } catch (Exception e) {
            getLogger().warning("Invalid \"Boss Bar Color\", Defaulted to \"PURPLE\"");
            barColor = BarColor.PURPLE;
        }

        try {
            barStyle = BarStyle.valueOf(getConfig().getString("Default Options.Boss Bar Style").toUpperCase());
        } catch (Exception e) {
            getLogger().warning("Invalid \"Boss Bar Style\", Defaulted to \"SOLID\"");
            barStyle = BarStyle.SOLID;
        }

    }

    public static BarColor getBarColor() {
        return barColor;
    }

    public static BarStyle getBarStyle() {
        return barStyle;
    }

    public static BossBarPlus getPlugin() {
        return bossBarPlus;
    }

}
