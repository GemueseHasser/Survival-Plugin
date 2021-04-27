package de.jonas.survival.handler.economy;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.File;

public final class EconomyHandler {

    @SneakyThrows
    public static void setMoney(
        @NotNull final Player player,
        @Range(from = 0, to = Integer.MAX_VALUE) final int amount
    ) {
        final File file = new File("plugins/Survival", "economy.yml");
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("Money" + player.getUniqueId().toString(), amount);

        cfg.save(file);
    }

    public static int getMoney(@NotNull final Player player) {
        final File file = new File("plugins/Survival", "economy.yml");
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        return cfg.getInt("Money" + player.getUniqueId().toString());
    }

}
