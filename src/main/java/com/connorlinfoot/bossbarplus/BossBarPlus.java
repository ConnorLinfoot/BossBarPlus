package com.connorlinfoot.bossbarplus;

import com.connorlinfoot.bossbarplus.BossBar.BossBarAPI;
import com.connorlinfoot.bossbarplus.Commands.BossBarCommand;
import com.connorlinfoot.bossbarplus.Handlers.ConfigHandler;
import com.connorlinfoot.bossbarplus.Listeners.PlayerJoin;
import com.connorlinfoot.bossbarplus.Listeners.PlayerQuit;
import com.connorlinfoot.bossbarplus.Util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BossBarPlus extends JavaPlugin {
	private static BossBarPlus bossBarPlus;
	private static ConfigHandler configHandler = new ConfigHandler();
	private int nextMessage = 0;
	private int announcerCounter = 0;

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

		if (getServer().getPluginManager().getPlugin("ViaVersion") != null) {
			BossBarAPI.setIsViaVersion(true);
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
		// Make sure to clear any boss bars otherwise if the server is reloading it will keep the old ones until the player logs out!
		BossBarAPI.clearBar();
		BossBarAPI.clearJoinBar();
	}

	private void startAnnouncerTask() {
		Runnable announcerRunnable = new Runnable() {
			@Override
			public void run() {
				double perSecond = 1 / (configHandler.isSmooth() ? configHandler.getAnnouncerTime() * 20 : configHandler.getAnnouncerTime());
				float percentage = (float) (perSecond * ((configHandler.isSmooth() ? configHandler.getAnnouncerTime() * 20 : configHandler.getAnnouncerTime()) - announcerCounter));
				String message = ChatColor.translateAlternateColorCodes('&', configHandler.getAnnouncerMessages().get(nextMessage));
				BossBarAPI.sendBar(message, configHandler.getAnnouncerColor(), configHandler.getAnnouncerStyle(), percentage);
				if (announcerCounter == (configHandler.isSmooth() ? configHandler.getAnnouncerTime() * 20L : configHandler.getAnnouncerTime())) {
					nextMessage++;
					announcerCounter = 0;
					if (nextMessage > configHandler.getAnnouncerMessages().size() - 1)
						nextMessage = 0;
				} else {
					announcerCounter++;
				}
			}
		};
		Bukkit.getScheduler().runTaskTimer(this, announcerRunnable, 0L, configHandler.isSmooth() ? 1L : 20L);
	}

}
