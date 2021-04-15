package stamina;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStaminaDepletedEvent extends Event implements Cancellable {
    private final PlayerStamina playerStamina;

    private boolean cancelled;

    public PlayerStaminaDepletedEvent(PlayerStamina playerStamina) {
        this.playerStamina = playerStamina;
    }

    public Player getPlayer() {
        return this.playerStamina.getPlayer();
    }

    public float getStamina() {
        return this.playerStamina.getStamina();
    }

    public float getMaxStamina() {
        return this.playerStamina.getMaxStamina();
    }

    public float getRecoverRate() {
        return this.playerStamina.getRecoverRate();
    }

    public float getTireRate() {
        return this.playerStamina.getTireRate();
    }

    public float getSlownessThreshold() {
        return this.playerStamina.getSlownessThreshold();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

