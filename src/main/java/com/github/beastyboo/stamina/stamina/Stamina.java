package com.github.beastyboo.stamina.stamina;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Stamina{

    private final JavaPlugin plugin;
    private final Logger logger;
    private final YamlPortConfiguration<ConfigPort> configManager;
    private final ConfigPort config;
    private final Map<UUID, StaminaPlayer> staminaPlayerMap;

    private final PotionEffect slownessPotion;
    private final PotionEffect jumpDenyPotion;

    public Stamina(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.configManager = YamlPortConfiguration.create(plugin.getDataFolder().toPath(), "config.yml", ConfigPort.class);
        this.configManager.reloadConfig();
        this.config = configManager.getConfigData();
        this.staminaPlayerMap = new HashMap<>();

        this.slownessPotion = new PotionEffect(PotionEffectType.SLOW, 2, 3, true, false);
        this.jumpDenyPotion = new PotionEffect(PotionEffectType.JUMP, 2, 250, true, false);
    }

    void start() {
        logger.info("Starting up Stamina");
        for (Map.Entry<String, Double> entry : config.rankMap().entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            if(Bukkit.getPluginManager().getPermission(key + "." + value) == null) {
                Bukkit.getPluginManager().addPermission(new Permission(key + "." + value));
            }
        }
        plugin.getServer().getPluginManager().registerEvents(new StaminaEvents(this), plugin);
        updateStaminaPlayers();
    }

    void close() {
        logger.info("Closing Stamina");
    }

    private void updateStaminaPlayers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
            for(StaminaPlayer player : staminaPlayerMap.values()) {
                if(!player.isActive()) {
                    double newStamina = player.getCurrentStaminaLevel() + config.recoveryRate();
                    if(newStamina > player.getMaxStaminaLevel()) {
                        player.setCurrentStaminaLevel(player.getMaxStaminaLevel());
                    } else {
                        player.setCurrentStaminaLevel(newStamina);
                    }
                }
            }
        }, 0L, 1L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
            for(StaminaPlayer staminaPlayer : staminaPlayerMap.values()) {
                Player player = Bukkit.getPlayer(staminaPlayer.getUuid());

                if(player.getGameMode().equals(GameMode.CREATIVE)) {
                    continue;
                }

                if(player.isSprinting()) {
                    staminaPlayer.setActive(true);
                    double newStamina = staminaPlayer.getCurrentStaminaLevel() - config.tireRate();
                    if(newStamina <= 0.0) {
                        staminaPlayer.setCurrentStaminaLevel(0.0);
                        player.setSprinting(false);
                    } else {
                        staminaPlayer.setCurrentStaminaLevel(newStamina);
                    }
                } else {
                    staminaPlayer.setActive(false);
                }
                if(staminaPlayer.getCurrentStaminaLevel() <= config.slownessThreshold()) {
                    if(!player.hasPotionEffect(PotionEffectType.SLOW)) {
                        player.addPotionEffect(slownessPotion);
                    }
                    if(!player.hasPotionEffect(PotionEffectType.JUMP)) {
                        player.addPotionEffect(jumpDenyPotion);
                    }
                } else {
                    if(player.hasPotionEffect(PotionEffectType.SLOW)) {
                        player.removePotionEffect(PotionEffectType.SLOW);
                    }
                    if(player.hasPotionEffect(PotionEffectType.JUMP)) {
                        player.removePotionEffect(PotionEffectType.JUMP);
                    }
                }

                float xpMultiplier = (float) (1.0f / staminaPlayer.getMaxStaminaLevel());
                player.setExp((float) staminaPlayer.getCurrentStaminaLevel() * xpMultiplier);
            }
        }, 0L, 1L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
            for(StaminaPlayer staminaPlayer : staminaPlayerMap.values()) {
                Player player = Bukkit.getPlayer(staminaPlayer.getUuid());

                if(player.getGameMode().equals(GameMode.CREATIVE)) {
                    continue;
                }

                if (player.getVelocity().getY() >= 0 && !player.isOnGround() ) {
                    double newStamina = staminaPlayer.getCurrentStaminaLevel() - config.jumpDepletionValue();
                    if(newStamina <= 0.0) {
                        staminaPlayer.setCurrentStaminaLevel(0.0);
                        player.setSprinting(false);
                    } else {
                        staminaPlayer.setCurrentStaminaLevel(newStamina);
                    }
                }
            }
        }, 0L, 13L);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigPort getConfig() {
        return config;
    }

    public Map<UUID, StaminaPlayer> getStaminaPlayerMap() {
        return staminaPlayerMap;
    }
}
