package com.connorlinfoot.bossbarplus.Handlers;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class ConfigHandler {
    private boolean smooth = false;
    private boolean joinEnabled = false;
    private double joinTime = 0;
    private BarColor defaultColor = BarColor.PURPLE;
    private BarStyle defaultStyle = BarStyle.SOLID;

    public void loadConfig(FileConfiguration config, Logger logger) {
        setSmooth(config.getBoolean("Smooth Animations"));

        try {
            setDefaultColor(BarColor.valueOf(config.getString("Default Options.Boss Bar Color").toUpperCase()));
        } catch (Exception e) {
            logger.warning("Invalid \"Boss Bar Color\", Defaulted to \"PURPLE\"");
            setDefaultColor(BarColor.PURPLE);
        }

        try {
            setDefaultStyle(BarStyle.valueOf(config.getString("Default Options.Boss Bar Style").toUpperCase()));
        } catch (Exception e) {
            logger.warning("Invalid \"Boss Bar Style\", Defaulted to \"SOLID\"");
            setDefaultStyle(BarStyle.SOLID);
        }

        setJoinEnabled(config.getBoolean("Broadcast on Join.Enabled"));
        if (isJoinEnabled()) {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Broadcast on Join.Message"));
            setJoinTime(config.getDouble("Broadcast on Join.Time"));

            BarColor joinColor;
            try {
                joinColor = BarColor.valueOf(config.getString("Broadcast on Join.Color").toUpperCase());
            } catch (Exception e) {
                logger.warning("Invalid \"Broadcast on Join.Color\", Defaulted to \"BLUE\"");
                joinColor = BarColor.BLUE;
            }

            BarStyle joinStyle;
            try {
                joinStyle = BarStyle.valueOf(config.getString("Broadcast on Join.Style").toUpperCase());
            } catch (Exception e) {
                logger.warning("Invalid \"Broadcast on Join.Style\", Defaulted to \"SOLID\"");
                joinStyle = BarStyle.SOLID;
            }

            BossBarAPI.setupJoinBossBar(joinMessage, getJoinTime(), joinColor, joinStyle);
        }
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setJoinEnabled(boolean joinEnabled) {
        this.joinEnabled = joinEnabled;
    }

    public boolean isJoinEnabled() {
        return joinEnabled;
    }

    public void setJoinTime(double joinTime) {
        this.joinTime = joinTime;
    }

    public double getJoinTime() {
        return joinTime;
    }

    public void setDefaultColor(BarColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public BarColor getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultStyle(BarStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public BarStyle getDefaultStyle() {
        return defaultStyle;
    }

}
