package de.jonas;

import de.jonas.survival.commands.AdminCommands;
import de.jonas.survival.commands.PlayerCommands;
import de.jonas.survival.handler.commands.CommandHandler;
import de.jonas.survival.task.ScoreboardTask;
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
                PlayerCommands.class,
            }
        );
        getLogger().info(
            "Registered all commands."
        );

        new ScoreboardTask().runTaskTimer(
            this,
            10,
            20
        );

        getLogger().info(
            "Schedule periodic scoreboard updating"
        );

        getLogger().info(
            "The plugin has been activated successful!"
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
