package com.connorlinfoot.bossbarplus.BossBar;

import com.connorlinfoot.bossbarplus.BossBarPlus;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BossBarAPI {
	private static BossBar announcerBossBar = null;
	private static BossBar joinBossBar = null;
	private static boolean viaVersion = false;
	private static double currentTime = 0;
	private static int taskID = 0;
	private static boolean perTick = BossBarPlus.getConfigHandler().isSmooth(); // If true it will run 20 times per second!

	public static void broadcastBar(final String message, double seconds, final BarColor barColor, final BarStyle barStyle, final String permission) {
		broadcastBar(message, seconds, barColor, barStyle, permission, BossBarPlus.getConfigHandler().isSoundEnabled(), BossBarPlus.getConfigHandler().getDefaultSound());
	}

	public static void broadcastBar(final String message, double seconds, final BarColor barColor, final BarStyle barStyle, final String permission, boolean soundEnabled, Sound sound) {
		Bukkit.getScheduler().cancelTask(taskID);
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
				if (permission != null && !permission.equals("") && player.hasPermission(permission))
					continue;
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
				sendBar(message, barColor, barStyle, (float) (currentTime * perTime));
				currentTime--;
			}
		};

		boolean barCurrentlyRunning = true;
		if (perTick) {
			taskID = Bukkit.getScheduler().runTaskTimer(BossBarPlus.getBossBarPlus(), runnable, 0L, 1L).getTaskId();
		} else {
			taskID = Bukkit.getScheduler().runTaskTimer(BossBarPlus.getBossBarPlus(), runnable, 0L, 20L).getTaskId();
		}
	}

	public static void sendBar(Player player, String message) {
		sendBar(player, message, BarColor.BLUE, BarStyle.SEGMENTED_6, 1);
	}

	public static void sendBar(String message, BarColor barColor, BarStyle barStyle, float percentage) {
		sendBar(message, barColor, barStyle, percentage, null);
	}

	public static void sendBar(String message, BarColor barColor, BarStyle barStyle, float percentage, String permission) {
		if (announcerBossBar == null) {
			announcerBossBar = getBossBar(message, barColor, barStyle, percentage);
		} else {
			announcerBossBar.setTitle(message);
			announcerBossBar.setColor(barColor);
			announcerBossBar.setStyle(barStyle);
			announcerBossBar.setProgress(percentage);
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (permission != null && !permission.equals("") && player.hasPermission(permission))
				continue;
			announcerBossBar.addPlayer(player);
		}
	}

	public static void sendBar(Player player, String message, BarColor barColor, BarStyle barStyle) {
		sendBar(player, message, barColor, barStyle, 1);
	}

	private static BossBar getBossBar(String message, BarColor barColor, BarStyle barStyle, float progress) {
		BossBar bossBar;
		if (viaVersion)
			bossBar = new BossBarVia(message, barColor, barStyle, progress);
		else
			bossBar = new BossBar1_9(message, barColor, barStyle, progress);
		return bossBar;
	}

	public static void sendBar(Player player, String message, BarColor barColor, BarStyle barStyle, float progress) {
		final BossBar bossBar = getBossBar(message, barColor, barStyle, progress);

		bossBar.addPlayer(player);
		Bukkit.getScheduler().runTaskLater(BossBarPlus.getBossBarPlus(), new Runnable() {
			@Override
			public void run() {
				bossBar.setProgress(0.5f);
			}
		}, 100L);
	}

	public static boolean isViaVersion() {
		return viaVersion;
	}

	public static void setViaVersion(boolean viaVersion) {
		BossBarAPI.viaVersion = viaVersion;
	}

	public static void clearBar() {
		if (announcerBossBar != null)
			announcerBossBar.clearBar();
	}

	public static void clearJoinBar() {
		if (joinBossBar != null)
			joinBossBar.clearBar();
	}

	public static void setupJoinBossBar(String message, double time, BarColor barColor, BarStyle barStyle) {
		if (joinBossBar == null) {
			joinBossBar = getBossBar(message, barColor, barStyle, 1);
		}

		if (Bukkit.getOnlinePlayers().size() > 0 && time <= 0) {
			// In case the plugin was reloaded and we should always show the bar
			for (Player player : Bukkit.getOnlinePlayers()) {
				joinBossBar.addPlayer(player);
			}
		}
	}

	public static BossBar getJoinBossBar() {
		return joinBossBar;
	}

	public static BossBar getAnnouncerBossBar() {
		return announcerBossBar;
	}

}
