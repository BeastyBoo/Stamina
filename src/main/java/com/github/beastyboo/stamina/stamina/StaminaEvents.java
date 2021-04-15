package com.github.beastyboo.stamina.stamina;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class StaminaEvents implements Listener {

    private final Stamina core;

    public StaminaEvents(Stamina core) {
        this.core = core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        double highest = core.getConfig().maxStaminaDefault();
        for (Map.Entry<String, Double> entry : core.getConfig().rankMap().entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            String permission = key + "." + value;

            if(player.hasPermission(permission)) {
                if(highest < value) {
                    highest = value;
                }
            }
        }

        core.getStaminaPlayerMap().put(uuid, new StaminaPlayer(uuid, highest));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        StaminaPlayer player = core.getStaminaPlayerMap().get(uuid);

        if(player != null) {
            core.getStaminaPlayerMap().remove(uuid, player);
        }
    }

}
