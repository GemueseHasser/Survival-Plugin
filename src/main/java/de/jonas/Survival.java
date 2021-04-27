package de.jonas;

import de.jonas.survival.commands.AdminCommands;
import de.jonas.survival.handler.commands.CommandHandler;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.format.DateTimeFormatter;

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

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println(formatter.toString());

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.register(
            new Class[]{
                AdminCommands.class,
            }
        );
        getLogger().info(
            "Es wurden alle Befehle registriert."
        );

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
        return ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "["
            + ChatColor.DARK_BLUE + "Survival"
            + ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "]"
            + ChatColor.GRAY + " ";
    }
}
