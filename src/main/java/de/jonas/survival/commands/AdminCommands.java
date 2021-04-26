package de.jonas.survival.commands;

import de.jonas.Survival;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class AdminCommands {

    @SurvivalCommand(
        command = "sign",
        minLength = 1,
        maxLength = Integer.MAX_VALUE,
        permission = "survival.sign",
        usage = "/sign <message>"
    )
    public void sign(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final ItemStack hand = player.getItemInHand();

        if (hand.getType().equals(Material.AIR)) {
            player.sendMessage(
                Survival.getPrefix() + "Du musst ein Item in der Hand halten!"
            );
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (@NotNull final String arg : args) {
            builder.append(arg).append(" ");
        }

        final ItemMeta meta = hand.getItemMeta();

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        final LocalDateTime time = LocalDateTime.now();

        final List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.translateAlternateColorCodes('&', builder.toString()));
        lore.add(" ");
        lore.add("------------------");
        lore.add(ChatColor.GRAY + "Signiert von " + player.getPlayerListName());
        lore.add(ChatColor.GRAY + "(" + formatter.format(time) + ")");

        meta.setLore(lore);
        hand.setItemMeta(meta);

        player.sendMessage(
            Survival.getPrefix() + "Das Item wurde erfolgreich signiert!"
        );
    }

}
