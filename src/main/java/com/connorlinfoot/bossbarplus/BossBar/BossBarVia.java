package com.connorlinfoot.bossbarplus.BossBar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.ViaVersion;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

import java.util.UUID;

public class BossBarVia extends com.connorlinfoot.bossbarplus.BossBar.BossBar {
	private BossBar bossBar;

	public BossBarVia(String text, BarColor color, BarStyle style, float progress) {
		if (color == null)
			color = BarColor.PINK;
		if (style == null)
			style = BarStyle.SOLID;
		bossBar = ViaVersion.getInstance().createBossBar(text, progress, BossColor.valueOf(color.toString()), BossStyle.valueOf(style.toString()));
		bossBar.show();
	}

	public void addPlayer(Player player) {
		bossBar.addPlayer(player);
	}

	public void removePlayer(Player player) {
		bossBar.removePlayer(player);
	}

	public void setTitle(String title) {
		bossBar.setTitle(title);
	}

	public void setProgress(float progress) {
		bossBar.setHealth(progress);
	}

	public void setColor(BarColor barColor) {
		bossBar.setColor(BossColor.valueOf(barColor.toString()));
	}

	public void setStyle(BarStyle barStyle) {
		bossBar.setStyle(BossStyle.valueOf(barStyle.toString()));
	}

	public void clearBar() {
		for (UUID uuid : bossBar.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null)
				continue;
			bossBar.removePlayer(player);
		}
	}

}
