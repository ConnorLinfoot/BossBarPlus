package com.connorlinfoot.bossbarplus.Handlers;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class ConfigHandler {
    private boolean smooth = false;
    private boolean joinEnabled = false;
    private boolean soundEnabled = false;
    private double joinTime = 0;
    private BarColor defaultColor = BarColor.PURPLE;
    private BarStyle defaultStyle = BarStyle.SOLID;
    private Sound defaultSound = Sound.ENTITY_ENDERDRAGON_GROWL;

    public void loadConfig(FileConfiguration config, Logger logger) {
        setSmooth(config.getBoolean("Smooth Animations"));
        setSoundEnabled(config.getBoolean("Default Options.Enable Sound"));

        try {
            setDefaultSound(Sound.valueOf(config.getString("Default Options.Sound").toUpperCase()));
        } catch (Exception e) {
            logger.warning("Invalid \"Sound\", Defaulted to \"ENTITY_ENDERDRAGON_GROWL\"");
            setDefaultSound(Sound.ENTITY_ENDERDRAGON_GROWL);
        }

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

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public boolean isJoinEnabled() {
        return joinEnabled;
    }

    public void setJoinEnabled(boolean joinEnabled) {
        this.joinEnabled = joinEnabled;
    }

    public double getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(double joinTime) {
        this.joinTime = joinTime;
    }

    public BarColor getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(BarColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public BarStyle getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(BarStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public Sound getDefaultSound() {
        return defaultSound;
    }

    public void setDefaultSound(Sound defaultSound) {
        this.defaultSound = defaultSound;
    }

}
