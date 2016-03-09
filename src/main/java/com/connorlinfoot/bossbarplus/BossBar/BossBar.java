package com.connorlinfoot.bossbarplus.BossBar;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class BossBar {
	ArrayList<String> players = new ArrayList<>();

	public void addPlayer(Player player) {
		players.add(player.getName());
	}

	public void removePlayer(Player player) {
		if (players.contains(player.getName()))
			players.remove(player.getName());
	}

	public void setTitle(String title) {
	}

	public void setProgress(float progress) {
	}

	public void setColor(BarColor barColor) {
	}

	public void setStyle(BarStyle barStyle) {
	}

	public void clearBar() {
	}

}
