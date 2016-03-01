package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import com.connorlinfoot.bossbarplus.Listeners.PlayerJoin;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
    private static BossBarPlus bossBarPlus;
    private static BarColor barColor = BarColor.PURPLE;
    private static BarStyle barStyle = BarStyle.SOLID;

    private static boolean joinEnabled = false;
    private static String joinMessage = "";
    private static BarColor joinColor = BarColor.BLUE;
    private static BarStyle joinStyle = BarStyle.SOLID;
    private static double joinTime = 0;

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

        joinEnabled = getConfig().getBoolean("Broadcast on Join.Enabled");
        if( joinEnabled ) {
            joinMessage = getConfig().getString("Broadcast on Join.Message");
            joinTime = getConfig().getDouble("Broadcast on Join.Time");

            try {
                joinColor = BarColor.valueOf(getConfig().getString("Broadcast on Join.Color").toUpperCase());
            } catch (Exception e) {
                getLogger().warning("Invalid \"Broadcast on Join.Color\", Defaulted to \"BLUE\"");
                joinColor = BarColor.BLUE;
            }

            try {
                joinStyle = BarStyle.valueOf(getConfig().getString("Broadcast on Join.Style").toUpperCase());
            } catch (Exception e) {
                getLogger().warning("Invalid \"Broadcast on Join.Style\", Defaulted to \"SOLID\"");
                joinStyle = BarStyle.SOLID;
            }

            BossBarAPI.createJoinBossBar(joinMessage, joinTime, joinColor, joinStyle);
        }


        getServer().getPluginCommand("bbp").setExecutor(new BossBarCommand());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

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

    public static double getJoinTime() {
        return joinTime;
    }

    public static boolean isJoinEnabled() {
        return joinEnabled;
    }

}
