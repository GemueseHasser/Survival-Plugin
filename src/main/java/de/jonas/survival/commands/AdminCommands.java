package de.jonas.survival.commands;

import de.jonas.Survival;
import de.jonas.survival.handler.economy.EconomyHandler;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import de.jonas.survival.objects.util.ItemCreator;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class AdminCommands {

    //<editor-fold desc="CONSTANTS">
    /** Die Nachricht, die einem User gesendet wird, wenn der eingegebene andere User nicht online ist. */
    @NotNull
    private static final String PLAYER_NOT_ONLINE = "Der Spieler ist nicht auf diesem Netwerk online!";

    private static final List<UUID> VANISH = new ArrayList<>();

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
        usage = "/gamemode <1|2|3>"
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

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser") && !isSame) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler entscheidet seinen Gamemode selbst xD"
            );
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

    //<editor-fold desc="command: armor">
    @SurvivalCommand(
        command = "armor",
        permission = "survival.armor",
        usage = "/armor"
    )
    public void armor(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final List<Enchantment> enchantments = new ArrayList<>();

        enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantments.add(Enchantment.PROTECTION_FIRE);
        enchantments.add(Enchantment.PROTECTION_PROJECTILE);
        enchantments.add(Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.add(Enchantment.THORNS);
        enchantments.add(Enchantment.DURABILITY);

        final ItemStack helmet = new ItemCreator(
            Material.DIAMOND_HELMET,
            "Helm",
            enchantments
        ).getStack();

        final ItemStack chestplate = new ItemCreator(
            Material.DIAMOND_CHESTPLATE,
            "Brustplatte",
            enchantments
        ).getStack();

        final ItemStack leggings = new ItemCreator(
            Material.DIAMOND_LEGGINGS,
            "Hose",
            enchantments
        ).getStack();

        enchantments.add(Enchantment.PROTECTION_FALL);
        enchantments.add(Enchantment.WATER_WORKER);

        final ItemStack boots = new ItemCreator(
            Material.DIAMOND_BOOTS,
            "Schuhe",
            enchantments
        ).getStack();

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);

        player.sendMessage(
            Survival.getPrefix() + "Dir wurde die Rüstung angezogen!"
        );
    }
    //</editor-fold>

    //<editor-fold desc="command: economy">
    @SurvivalCommand(
        command = "economy",
        minLength = 3,
        maxLength = 3,
        permission = "survival.economy",
        usage = "/economy <set|add|remove> <player> <amount>"
    )
    public void economy(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final Player target = Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler ist nicht online!"
            );
            return;
        }

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser")) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler möchte seinen Kontostand beibehalten xD"
            );
            return;
        }

        final int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (@NotNull final IllegalArgumentException ignored) {
            player.sendMessage(
                Survival.getPrefix() + "Bitte wähle einen gültigen Betrag!"
            );
            return;
        }

        switch (args[0]) {
            case "set":
                EconomyHandler.setMoney(target, amount);
                player.sendMessage(
                    Survival.getPrefix() + "Der Kontostand von " + target.getName() + " wurde auf " + amount +
                        " gesetzt!"
                );
                break;

            case "add":
                EconomyHandler.setMoney(
                    target,
                    EconomyHandler.getMoney(target) + amount
                );
                player.sendMessage(
                    Survival.getPrefix() + "Dem Kontostand von " + target.getName() + " wurde " + amount +
                        " hinzugefügt!"
                );
                break;

            case "remove":
                EconomyHandler.setMoney(
                    target,
                    EconomyHandler.getMoney(target) - amount
                );
                player.sendMessage(
                    Survival.getPrefix() + "Dem Kontostand von " + target.getName() + " wurde " + amount +
                        " abgezogen!"
                );
                break;

            default:
                player.sendMessage(
                    Survival.getPrefix() + "Bitte benutze /economy <set|add|remove> <player> <amount>"
                );
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="command: money">
    @SurvivalCommand(
        command = "money",
        minLength = 1,
        maxLength = 1,
        permission = "survival.money",
        usage = "/money <player>"
    )
    public void money(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        final Player target = Bukkit.getPlayer(args[0]);

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser")) {
            player.sendMessage(
                Survival.getPrefix() + "Nö"
            );
            return;
        }

        final int amount = EconomyHandler.getMoney(target);

        player.sendMessage(
            Survival.getPrefix() + "Der Kontostand des Spielers " + target.getName() + " beträgt " + amount + "!"
        );
    }
    //</editor-fold>

    @SurvivalCommand(
        command = "sudo",
        minLength = 2,
        maxLength = Integer.MAX_VALUE,
        permission = "survival.sudo",
        usage = "/sudo <player> <command>"
    )
    public void sudo(
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

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser")) {
            player.sendMessage(
                Survival.getPrefix() + "Der Spieler führt seine Befehle selbst aus xD"
            );
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        target.performCommand(builder.toString());

        player.sendMessage(
            Survival.getPrefix() + "Der Befehl wurde ausgeführt!"
        );
    }

    //<editor-fold desc="command: crash">
    @SurvivalCommand(
        command = "crash",
        minLength = 1,
        maxLength = 1,
        permission = "survival.crash",
        usage = "/crash <player>"
    )
    public void crash(
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

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser")) {
            player.sendMessage(
                Survival.getPrefix() + "Nein ich glaube nicht, dass du den Spieler crashen möchtest."
            );
            return;
        }

        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(
            new PacketPlayOutExplosion(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Float.MAX_VALUE,
                Collections.EMPTY_LIST,
                new Vec3D(
                    Double.MAX_VALUE,
                    Double.MAX_VALUE,
                    Double.MAX_VALUE
                )
            )
        );

        player.sendMessage(
            Survival.getPrefix() + "Der Spieler " + target.getName() + " wurde gecrashed!"
        );
    }
    //</editor-fold>

    //<editor-fold desc="command: invsee">
    @SurvivalCommand(
        command = "invsee",
        minLength = 1,
        maxLength = 1,
        permission = "survival.invsee",
        usage = "/invsee <player>"
    )
    public void invsee(
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

        if (target.getName().equalsIgnoreCase("Gemuese_Hasser")) {
            player.sendMessage(
                Survival.getPrefix() + "Das ist nicht dein Inventar!"
            );
            return;
        }

        player.openInventory(target.getInventory());
    }
    //</editor-fold>

    //<editor-fold desc="command: vanish">
    @SurvivalCommand(
        command = "vanish",
        permission = "survival.vanish",
        usage = "/vanish"
    )
    public void vanish(
        @NotNull final Player player,
        @NotNull final String[] args
    ) {
        if (VANISH.contains(player.getUniqueId())) {
            for (@NotNull final Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(
                    ChatColor.YELLOW + player.getName() + " joined the game"
                );
                if (all.getName().equalsIgnoreCase("Gemuese_Hasser")) {
                    all.sendMessage(
                        Survival.getPrefix() + "Der Spieler " + player.getName() + " hat sich wieder sichbar "
                            + "gemacht!"
                    );
                    continue;
                }
                all.showPlayer(player);
            }
            VANISH.remove(player.getUniqueId());
            player.sendMessage(
                Survival.getPrefix() + "Du bist nun wieder sichtbar!"
            );
        } else {
            VANISH.add(player.getUniqueId());
            for (@NotNull final Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage(
                    ChatColor.YELLOW + player.getName() + " left the game"
                );
                if (all.getName().equalsIgnoreCase("Gemuese_Hasser")) {
                    all.sendMessage(
                        Survival.getPrefix() + "Der Spieler " + player.getName() + " hat sich unsichtbar gemacht!"
                    );
                    continue;
                }
                all.hidePlayer(player);
            }
            player.sendMessage(
                Survival.getPrefix() + "Du bist nun unsichtbar!"
            );
        }
    }
    //</editor-fold>

    @SurvivalCommand(
        command = "rename",
        minLength = 1,
        maxLength = Integer.MAX_VALUE,
        permission = "survival.rename",
        usage = "/rename <name>"
    )
    public void rename(
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
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', builder.toString()));

        hand.setItemMeta(meta);

        player.sendMessage(
            Survival.getPrefix() + "Das Item wurde erfolgreich umbenannt!"
        );
    }

}
