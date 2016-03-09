package com.connorlinfoot.bossbarplus.Listeners;

import com.connorlinfoot.bossbarplus.BossBar.BossBarAPI;
import com.connorlinfoot.bossbarplus.BossBarPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (BossBarAPI.getAnnouncerBossBar() != null) {
			BossBarAPI.getAnnouncerBossBar().addPlayer(event.getPlayer());
		}

		if (!BossBarPlus.getConfigHandler().isJoinEnabled() || BossBarAPI.getJoinBossBar() == null) {
			if (BossBarPlus.getConfigHandler().isDebug())
				Bukkit.getLogger().info("BossBar is not enabled or no Boss Bar for join exists!");
			return;
		}

		BossBarAPI.getJoinBossBar().addPlayer(event.getPlayer());
		if (BossBarPlus.getConfigHandler().getJoinTime() > 0) {
			final UUID playerUUID = event.getPlayer().getUniqueId();
			Bukkit.getScheduler().runTaskLater(BossBarPlus.getBossBarPlus(), new Runnable() {
				@Override
				public void run() {
					Player player = Bukkit.getPlayer(playerUUID);
					if (player == null)
						return;
					BossBarAPI.getJoinBossBar().removePlayer(player);
				}
			}, (long) (BossBarPlus.getConfigHandler().getJoinTime() * 20L));
		}
	}

}
