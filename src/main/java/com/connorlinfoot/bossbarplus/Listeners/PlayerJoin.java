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
        if(!BossBarPlus.isJoinEnabled() || BossBarAPI.getGlobalJoinBossBar() == null)
            return;

        BossBarAPI.getGlobalJoinBossBar().addPlayer(event.getPlayer());
        if( BossBarPlus.getJoinTime() > 0 ) {
            BossBarAPI.getGlobalJoinBossBar().addPlayer(event.getPlayer());
        }
        final UUID playerUUID = event.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskLaterAsynchronously(BossBarPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerUUID);
                if(player == null)
                    return;
                BossBarAPI.getGlobalJoinBossBar().removePlayer(player);
            }
        }, (long) (BossBarPlus.getJoinTime() * 20L));
    }

}
