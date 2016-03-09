package com.connorlinfoot.bossbarplus.Listeners;

import com.connorlinfoot.bossbarplus.BossBar.BossBarAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (BossBarAPI.getAnnouncerBossBar() != null) {
			BossBarAPI.getAnnouncerBossBar().removePlayer(event.getPlayer());
		}
		if (BossBarAPI.getJoinBossBar() != null) {
			BossBarAPI.getJoinBossBar().removePlayer(event.getPlayer());
		}
	}

}
