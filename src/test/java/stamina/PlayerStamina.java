package stamina;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerStamina
{
    private static final PotionEffect slownessPotion;
    private static final PotionEffect jumpDenyPotion;
    private final StaminaPlugin plugin;
    private Player player;
    private boolean bypass;
    private final float maxStamina;
    private final float recoverRate;
    private final float tireRate;
    private final float slownessThreshold;
    private final float jumpDepletion;
    private float stamina;
    private DisplayType type;
    private long lastUpdateTime;
    private final float hungerMultiplier;
    private final float xpMultiplier;

    public PlayerStamina(final StaminaPlugin plugin, final Player player, final float maxStamina, final float recoverRate, final float tireRate, final float jumpDepletion, final float slownessThreshold, final DisplayType displayType) {
        this.bypass = false;
        this.lastUpdateTime = System.currentTimeMillis();
        this.plugin = plugin;
        this.player = player;
        this.maxStamina = maxStamina;
        this.stamina = maxStamina;
        this.recoverRate = recoverRate;
        this.tireRate = tireRate;
        this.jumpDepletion = jumpDepletion;
        this.slownessThreshold = slownessThreshold;
        this.type = displayType;
        this.hungerMultiplier = 20.0f / maxStamina;
        this.xpMultiplier = 1.0f / maxStamina;
    }

    void update() {
        if (this.bypass) {
            return;
        }
        if (System.currentTimeMillis() - this.lastUpdateTime >= 50L) {
            this.lastUpdateTime = System.currentTimeMillis();
            if (this.player.isSprinting() && (this.player.getGameMode() == GameMode.ADVENTURE || this.player.getGameMode() == GameMode.SURVIVAL)) {
                this.stamina = this.bound(this.stamina - this.tireRate);
            }
            else {
                this.stamina = this.bound(this.stamina + this.recoverRate);
            }
        }
        if (this.stamina <= this.slownessThreshold) {
            final PlayerStaminaDepletedEvent event = new PlayerStaminaDepletedEvent(this);
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                Bukkit.getServer().getPluginManager().callEvent((Event)event);
                if (!event.isCancelled()) {
                    this.player.addPotionEffect(PlayerStamina.slownessPotion);
                    this.player.addPotionEffect(PlayerStamina.jumpDenyPotion);
                }
                return;
            });
        }
        if (this.stamina <= 0.0f) {
            this.player.setSprinting(false);
        }
        this.display();
    }

    void jumped() {
        this.stamina = this.bound(this.stamina - this.jumpDepletion);
    }

    public void setBypass(final boolean bypass) {
        this.bypass = bypass;
    }

    private void display() {
        if (this.type == DisplayType.HUNGER) {
            this.player.setFoodLevel(Math.round(this.stamina * this.hungerMultiplier));
        }
        else if (this.type == DisplayType.XP) {
            this.player.setExp(this.stamina * this.xpMultiplier);
        }
    }

    private float bound(final float i) {
        return Math.max(0.0f, Math.min(this.maxStamina, i));
    }

    public Player getPlayer() {
        return this.player;
    }

    public float getMaxStamina() {
        return this.maxStamina;
    }

    public float getRecoverRate() {
        return this.recoverRate;
    }

    public float getTireRate() {
        return this.tireRate;
    }

    public float getSlownessThreshold() {
        return this.slownessThreshold;
    }

    public float getStamina() {
        return this.stamina;
    }

    static {
        slownessPotion = new PotionEffect(PotionEffectType.SLOW, 2, 3, true, false);
        jumpDenyPotion = new PotionEffect(PotionEffectType.JUMP, 2, 250, true, false);
    }

    public enum DisplayType
    {
        HUNGER,
        XP,
        NONE;

        public static DisplayType fromString(final String str) {
            final String upperCase = str.toUpperCase();
            switch (upperCase) {
                case "HUNGER": {
                    return DisplayType.HUNGER;
                }
                case "XP": {
                    return DisplayType.XP;
                }
                default: {
                    return DisplayType.NONE;
                }
            }
        }
    }
}
