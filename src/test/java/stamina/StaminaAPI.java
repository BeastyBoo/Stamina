package stamina;

import org.bukkit.entity.Player;

public class StaminaAPI {
    private static StaminaPlugin plugin;

    public StaminaAPI(StaminaPlugin pluginIn) {
        plugin = pluginIn;
    }

    public static void setPlayerBypass(Player player, boolean bypass) {
        plugin.getCachedPlayerStamina(player).setBypass(bypass);
    }
}
