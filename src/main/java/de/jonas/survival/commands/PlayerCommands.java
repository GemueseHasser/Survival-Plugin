package de.jonas.survival.commands;

import de.jonas.Survival;
import de.jonas.survival.handler.economy.EconomyHandler;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommands {

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

}
