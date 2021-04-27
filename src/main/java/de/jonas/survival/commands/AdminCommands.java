package de.jonas.survival.commands;

import de.jonas.Survival;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class AdminCommands {

    //<editor-fold desc="CONSTANTS">
    /** Die Nachricht, die einem User gesendet wird, wenn der eingegebene andere User nicht online ist. */
    @NotNull
    private static final String PLAYER_NOT_ONLINE = "Der Spieler ist nicht auf diesem Netwerk online!";

    //<editor-fold desc="command: gamemode">
    /** Die Nummer für Gamemode Überleben. */
    private static final int GAMEMODE_SURVIVAL = 0;
    /** Die Nummer für Gamemode Kreativ. */
    private static final int GAMEMODE_CREATIVE = 1;
    /** Die Nummer für Gamemode Abenteuer. */
    private static final int GAMEMODE_ADVENTURE = 2;
    /** Die Nummer für Gamemode Zuschauer. */
    private static final int GAMEMODE_SPECTATOR = 3;
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="command: sign">
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
    //</editor-fold>

    //<editor-fold desc="command: gamemode">
    @SurvivalCommand(
        command = "gamemode",
        minLength = 1,
        maxLength = 2,
        permission = "survival.gamemode",
        usage = "/gamemode <1|2|3>",
        aliases = {"gm"}
    )
    public void gamemode(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final boolean isSame = args.length == 1;
        final Player target = isSame ? player : Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(Survival.getPrefix() + PLAYER_NOT_ONLINE);
            return;
        }

        switch (args[0]) {
            case "survival":
            case "überleben":
            case GAMEMODE_SURVIVAL + "":
                target.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(
                    Survival.getPrefix() + getGamemodeMessage(GAMEMODE_SURVIVAL, isSame, target.getName())
                );
                break;

            case "creative":
            case "kreativ":
            case GAMEMODE_CREATIVE + "":
                target.setGameMode(GameMode.CREATIVE);
                player.sendMessage(
                    Survival.getPrefix() + getGamemodeMessage(GAMEMODE_CREATIVE, isSame, target.getName())
                );
                break;

            case "adventure":
            case "abenteuer":
            case GAMEMODE_ADVENTURE + "":
                target.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(
                    Survival.getPrefix() + getGamemodeMessage(GAMEMODE_ADVENTURE, isSame, target.getName())
                );
                break;

            case "spectator":
            case "zuschauer":
            case GAMEMODE_SPECTATOR + "":
                target.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(
                    Survival.getPrefix() + getGamemodeMessage(GAMEMODE_SPECTATOR, isSame, target.getName())
                );
                break;

            default:
                player.sendMessage(
                    Survival.getPrefix() + "Bitte benutze /gamemode <1|2|3>"
                );
                break;
        }
    }

    private String getGamemodeMessage(
        @Range(from = 0, to = Integer.MAX_VALUE) final int gm,
        final boolean same,
        @Nullable final String name
    ) {
        final String gamemode = gm == 0 ? "Überleben" : gm == 1 ? "Kreativ" : gm == 2 ? "Abenteuer" : "Zuschauer";
        return (same ? "Du wurdest " : "Der Spieler " + name + " wurde ")
            + "in Gamemode " + ChatColor.RED + gamemode + ChatColor.GRAY + " versetzt!";
    }
    //</editor-fold>

    //<editor-fold desc="command: enchant">
    @SurvivalCommand(
        command = "enchant",
        minLength = 2,
        maxLength = 2,
        permission = "survival.enchant",
        usage = "/enchant <enchantment> <level>"
    )
    public void enchant(
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

        final Enchantment enchantment;

        enchantment = Enchantment.getByName(args[0].toUpperCase());

        if (enchantment == null) {
            player.sendMessage(
                Survival.getPrefix() + "Bitte wähle eine gültige Verzauberung!"
            );
            return;
        }

        final int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (@NotNull final IllegalArgumentException ignored) {
            player.sendMessage(
                Survival.getPrefix() + "Bitte wähle einen gültigen Level!"
            );
            return;
        }

        final ItemMeta meta = hand.getItemMeta();
        meta.addEnchant(enchantment, level, true);

        hand.setItemMeta(meta);

        player.sendMessage(
            Survival.getPrefix() + "Du hast das Item erfolgreich verzaubert!"
        );
    }
    //</editor-fold>

}
