package com.github.beastyboo.stamina.stamina;

import java.util.Objects;
import java.util.UUID;

public class StaminaPlayer {

    private final UUID uuid;
    private final double maxStaminaLevel;
    private double currentStaminaLevel;
    private boolean isActive;

    public StaminaPlayer(UUID uuid, double maxStaminaLevel) {
        this.uuid = uuid;
        this.maxStaminaLevel = maxStaminaLevel;
        this.currentStaminaLevel = maxStaminaLevel;
        this.isActive = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getMaxStaminaLevel() {
        return maxStaminaLevel;
    }

    public double getCurrentStaminaLevel() {
        return currentStaminaLevel;
    }

    public void setCurrentStaminaLevel(double currentStaminaLevel) {
        this.currentStaminaLevel = currentStaminaLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaminaPlayer that = (StaminaPlayer) o;
        return Double.compare(that.maxStaminaLevel, maxStaminaLevel) == 0 && Double.compare(that.currentStaminaLevel, currentStaminaLevel) == 0 && isActive == that.isActive && uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, maxStaminaLevel, currentStaminaLevel, isActive);
    }

    @Override
    public String toString() {
        return "StaminaPlayer{" +
                "uuid=" + uuid +
                ", maxStaminaLevel=" + maxStaminaLevel +
                ", currentStaminaLevel=" + currentStaminaLevel +
                ", isActive=" + isActive +
                '}';
    }

}
