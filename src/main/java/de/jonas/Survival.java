package de.jonas;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Survival extends JavaPlugin {

    @Getter
    private static String prefix;

    @Getter
    private static Survival instance;

    @Override
    public void onEnable() {
        // declare instance
        instance = this;

        // declare prefix
        prefix = getGeneratedPrefix();

        getLogger().info(
            "Das Plugin wurde erfolgreich aktiviert!"
        );
    }

    @Override
    public void onDisable() {
        getLogger().info(
            "Das Plugin wurde deaktiviert!"
        );
    }

    private String getGeneratedPrefix() {
        return ChatColor.GRAY.toString() + ChatColor.BOLD + "["
            + ChatColor.DARK_BLUE + "Survival"
            + ChatColor.GRAY.toString() + ChatColor.BOLD + "]";
    }
}
