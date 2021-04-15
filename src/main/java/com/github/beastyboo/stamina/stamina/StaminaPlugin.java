package com.github.beastyboo.stamina.stamina;

import org.bukkit.plugin.java.JavaPlugin;

public class StaminaPlugin extends JavaPlugin {

    private Stamina core;

    @Override
    public void onEnable() {
        core = new Stamina(this);
        core.start();
    }

    @Override
    public void onDisable() {
        core.close();
        core = null;
    }


}
