package com.connorlinfoot.bossbarplus.Listeners;

import com.connorlinfoot.bossbarplus.BossBarAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (BossBarAPI.isBarCurrentlyRunning()) {
            BossBarAPI.getBossBar().removePlayer(event.getPlayer());
        }
    }

}
