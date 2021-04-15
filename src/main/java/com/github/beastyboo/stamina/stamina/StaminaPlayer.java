package com.github.beastyboo.stamina.stamina;

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
}
