package stamina;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class StaminaPlugin extends JavaPlugin implements Listener
{
    private PlayerStamina.DisplayType displayType;
    private float defaultMaxStamina;
    private float recoveryRate;
    private float tireRate;
    private float slownessThreshold;
    private float jumpDepletionValue;
    private boolean jumpDepletion;
    private HashMap<String, Float> maxStamina;
    private HashMap<UUID, PlayerStamina> stamina;

    public StaminaPlugin() {
        this.defaultMaxStamina = 20.0f;
        this.recoveryRate = 1.0f;
        this.tireRate = 0.2f;
        this.slownessThreshold = 3.0f;
        this.jumpDepletionValue = 1.0f;
        this.jumpDepletion = false;
        this.maxStamina = new HashMap<String, Float>();
        this.stamina = new HashMap<UUID, PlayerStamina>();
    }

    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        final String displayAs = this.getConfig().getString("display-bar", "XP");
        this.displayType = PlayerStamina.DisplayType.fromString(Objects.requireNonNull(displayAs));
        this.recoveryRate = (float)this.getConfig().getDouble("recovery-rate", 0.10000000149011612);
        this.tireRate = (float)this.getConfig().getDouble("tire-rate", 0.20000000298023224);
        this.slownessThreshold = (float)this.getConfig().getDouble("slowness-threshold", 3.0);
        this.jumpDepletion = this.getConfig().getBoolean("player-jump-depletion", true);
        this.jumpDepletionValue = (float)this.getConfig().getDouble("jump-depletion-value", 1.0);
        this.defaultMaxStamina = (float)this.getConfig().getDouble("max-stamina-default", 20.0);

        final ConfigurationSection rankValues = this.getConfig().getConfigurationSection("max-stamina-rank-values");
        if (rankValues != null) {
            for (final String key : rankValues.getKeys(false)) {
                this.maxStamina.put("stamina.rank." + key, (float)rankValues.getDouble(key));
            }
        }

        new StaminaAPI(this);
        this.getServer().getPluginManager().registerEvents((Listener)this, this);
        this.update();
    }

    public void onDisable() {
    }

    private void update() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> Bukkit.getOnlinePlayers().forEach(player -> Objects.requireNonNull(this.stamina.get(player.getUniqueId())).update()), 20L, 1L);
    }

    private PlayerStamina getPlayerStamina(final Player player) {
        return new PlayerStamina(this, player, this.getMaxStamina(player), this.recoveryRate, this.tireRate, this.jumpDepletionValue, this.slownessThreshold, this.displayType);
    }

    private float getMaxStamina(final Player player) {
        final AtomicDouble atomicDouble = new AtomicDouble(this.defaultMaxStamina);
        final AtomicDouble atomicDouble2 = null;
        this.maxStamina.forEach((key, value) -> {
            if (value > atomicDouble2.get() && player.hasPermission(key)) {
                atomicDouble2.set((double)value);
            }
            return;
        });
        return (float)atomicDouble.get();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void joinEvent(final PlayerJoinEvent e) {
        this.stamina.put(e.getPlayer().getUniqueId(), this.getPlayerStamina(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void leaveEvent(final PlayerQuitEvent e) {
        this.stamina.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void moveEvent(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        final Location from = e.getFrom();
        final Location to = e.getTo();
        if (to == null) {
            return;
        }
        final double jumpHeight = to.getY() - from.getY();
        if (this.jumpDepletion && jumpHeight >= 0.35 && jumpHeight <= 0.45 && !player.getEyeLocation().getBlock().isLiquid() && !player.isFlying() && (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL)) {
            this.getCachedPlayerStamina(player).jumped();
        }

    }

    PlayerStamina getCachedPlayerStamina(final Player player) {
        return this.stamina.get(player.getUniqueId());
    }
}
