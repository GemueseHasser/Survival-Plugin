package de.jonas.survival.commands;

import de.jonas.Survival;
import de.jonas.survival.handler.economy.EconomyHandler;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerCommands {

    public static final Map<UUID, Location> DEATH_LOCATIONS = new HashMap<>();

    //<editor-fold desc="command: pay">
    @SurvivalCommand(
        command = "pay",
        minLength = 2,
        maxLength = 2,
        permission = "survival.pay",
        usage = "/pay <player> <amount>"
    )
    public void pay(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler ist nicht online!"
            );
            return;
        }

        final int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (@NotNull final IllegalArgumentException ignored) {
            player.sendMessage(
                Survival.getPrefix() + "Bitte gib einen gültigen Betrag an!"
            );
            return;
        }

        EconomyHandler.setMoney(
            target,
            EconomyHandler.getMoney(target) + amount
        );

        EconomyHandler.setMoney(
            player,
            EconomyHandler.getMoney(player) - amount
        );

        player.sendMessage(
            Survival.getPrefix() + "Du hast dem Spieler " + target.getName() + " " + amount + " überwiesen!"
        );

        target.sendMessage(
            Survival.getPrefix() + "Der Spieler " + player.getName() + " hat dir " + amount + " überwiesen!"
        );
    }
    //</editor-fold>

    //<editor-fold desc="command: enderchest">
    @SurvivalCommand(
        command = "enderchest",
        minLength = 0,
        maxLength = 1,
        permission = "survival.enderchest",
        usage = "/enderchest"
    )
    public void enderchest(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
            return;
        }

        if (!player.hasPermission("survival.enderchest.other")) {
            player.sendMessage(
                Survival.getPrefix() + "Bitte benutze /enderchest"
            );
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler ist nicht online!"
            );
            return;
        }

        player.openInventory(target.getEnderChest());
    }
    //</editor-fold>

    //<editor-fold desc="home">
    @SneakyThrows
    @SurvivalCommand(
        command = "sethome",
        permission = "survival.home",
        usage = "/sethome"
    )
    public void setHome(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final File file = new File("plugins/Survival", "homes.yml");
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final String world = player.getWorld().getName();
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();

        cfg.set("Home." + player.getUniqueId().toString() + ".World", world);
        cfg.set("Home." + player.getUniqueId().toString() + ".X", x);
        cfg.set("Home." + player.getUniqueId().toString() + ".Y", y);
        cfg.set("Home." + player.getUniqueId().toString() + ".Z", z);

        cfg.save(file);

        player.sendMessage(
            Survival.getPrefix() + "Du hast dein Home gesetzt!"
        );
    }

    @SurvivalCommand(
        command = "home",
        permission = "survival.home",
        usage = "/home"
    )
    public void home(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final File file = new File("plugins/Survival", "homes.yml");
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        final World world = Bukkit.getWorld(cfg.getString("Home." + player.getUniqueId().toString() + ".World"));
        final int x = cfg.getInt("Home." + player.getUniqueId().toString() + ".X");
        final int y = cfg.getInt("Home." + player.getUniqueId().toString() + ".Y");
        final int z = cfg.getInt("Home." + player.getUniqueId().toString() + ".Z");

        final Location location = new Location(world, x, y, z);

        player.teleport(location);

        player.sendMessage(
            Survival.getPrefix() + "Du wurdest zu deinem Home teleportiert!"
        );
    }
    //</editor-fold>

    //<editor-fold desc="command: back">
    @SurvivalCommand(
        command = "back",
        permission = "survival.back",
        usage = "/back"
    )
    public void back(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (!DEATH_LOCATIONS.containsKey(player.getUniqueId())) {
            player.sendMessage(
                Survival.getPrefix() + "Du bist noch nicht gestorben!"
            );
            return;
        }

        player.teleport(DEATH_LOCATIONS.get(player.getUniqueId()));
        player.sendMessage(
            Survival.getPrefix() + "Du wurdest zu deinem Todes-Punkt teleportiert!"
        );
    }
    //</editor-fold>

    //<editor-fold desc="command: ping">
    @SurvivalCommand(
        command = "ping",
        permission = "survival.ping",
        usage = "/ping"
    )
    public void ping(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final int ping = ((CraftPlayer) player).getHandle().ping;
        player.sendMessage(
            Survival.getPrefix() + "Du hast einen Ping von " + ping + "!"
        );
    }
    //</editor-fold>

}
