package com.connorlinfoot.bossbarplus.Handlers;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ConfigHandler {
    private boolean debug = false;
    private boolean smooth = false;
    private boolean joinEnabled = false;
    private boolean soundEnabled = false;
    private boolean announcerEnabled = false;
    private double joinTime = 0;
    private double announcerTime = 20;
    private BarColor defaultColor = BarColor.PURPLE;
    private BarColor announcerColor = BarColor.GREEN;
    private BarStyle defaultStyle = BarStyle.SOLID;
    private BarStyle announcerStyle = BarStyle.SOLID;
    private Sound defaultSound = Sound.ENTITY_ENDERDRAGON_GROWL;
    private ArrayList<String> announcerMessages = new ArrayList<>();

    public void loadConfig(FileConfiguration config, Logger logger) {
        if (config.isSet("Debug"))
            setDebug(config.getBoolean("Debug"));
        setSmooth(config.getBoolean("Smooth Animations"));
        setJoinEnabled(config.getBoolean("Broadcast on Join.Enabled"));
        setSoundEnabled(config.getBoolean("Default Options.Enable Sound"));
        setAnnouncerEnabled(config.getBoolean("Announcer.Enabled"));

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

        try {
            setAnnouncerColor(BarColor.valueOf(config.getString("Announcer.Color").toUpperCase()));
        } catch (Exception e) {
            logger.warning("Invalid \"Announcer Color\", Defaulted to \"GREEN\"");
            setDefaultColor(BarColor.GREEN);
        }

        try {
            setAnnouncerStyle(BarStyle.valueOf(config.getString("Announcer.Style").toUpperCase()));
        } catch (Exception e) {
            logger.warning("Invalid \"Announcer Style\", Defaulted to \"SOLID\"");
            setDefaultStyle(BarStyle.SOLID);
        }

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

        if (isAnnouncerEnabled()) {
            setAnnouncerMessages((ArrayList<String>) config.getStringList("Announcer.Messages"));
            setAnnouncerTime(config.getDouble("Announcer.Time"));
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
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

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isAnnouncerEnabled() {
        return announcerEnabled;
    }

    public void setAnnouncerEnabled(boolean announcerEnabled) {
        this.announcerEnabled = announcerEnabled;
    }

    public double getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(double joinTime) {
        this.joinTime = joinTime;
    }

    public double getAnnouncerTime() {
        return announcerTime;
    }

    public void setAnnouncerTime(double announcerTime) {
        this.announcerTime = announcerTime;
    }

    public BarColor getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(BarColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public BarColor getAnnouncerColor() {
        return announcerColor;
    }

    public void setAnnouncerColor(BarColor announcerColor) {
        this.announcerColor = announcerColor;
    }

    public BarStyle getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(BarStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public BarStyle getAnnouncerStyle() {
        return announcerStyle;
    }

    public void setAnnouncerStyle(BarStyle announcerStyle) {
        this.announcerStyle = announcerStyle;
    }

    public Sound getDefaultSound() {
        return defaultSound;
    }

    public void setDefaultSound(Sound defaultSound) {
        this.defaultSound = defaultSound;
    }

    public void addAnnouncerMessage(String message) {
        this.announcerMessages.add(message);
    }

    public void clearAnnouncerMessages() {
        this.announcerMessages.clear();
    }

    public ArrayList<String> getAnnouncerMessages() {
        return announcerMessages;
    }

    public void setAnnouncerMessages(ArrayList<String> announcerMessages) {
        this.announcerMessages = announcerMessages;
    }

}
