package com.connorlinfoot.bossbarplus.Listeners;

import com.connorlinfoot.bossbarplus.BossBarAPI;
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
        if( BossBarAPI.isBarCurrentlyRunning() && ( BossBarAPI.getGlobalBossBarPerm() == null || !event.getPlayer().hasPermission(BossBarAPI.getGlobalBossBarPerm())) ) {
            BossBarAPI.getGlobalBossBar().addPlayer(event.getPlayer());
        }

        if(!BossBarPlus.getConfigHandler().isJoinEnabled() || BossBarAPI.getJoinBossBar() == null)
            return;

        BossBarAPI.getJoinBossBar().addPlayer(event.getPlayer());
        if( BossBarPlus.getConfigHandler().getJoinTime() > 0 ) {
            BossBarAPI.getJoinBossBar().addPlayer(event.getPlayer());
        }
        final UUID playerUUID = event.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskLaterAsynchronously(BossBarPlus.getBossBarPlus(), new Runnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerUUID);
                if(player == null)
                    return;
                BossBarAPI.getJoinBossBar().removePlayer(player);
            }
        }, (long) (BossBarPlus.getConfigHandler().getJoinTime() * 20L));
    }

}
