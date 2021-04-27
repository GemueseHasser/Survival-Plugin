package de.jonas.survival.handler.scoreboard;

import de.jonas.survival.handler.economy.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public final class ScoreboardHandler {

    public static void setScoreboard(@NotNull final Player player) {
        // create new scoreboard
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        // register objective
        final Objective objective = scoreboard.registerNewObjective("abcde", "abcde");

        // set objective properties
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(
            ChatColor.GOLD.toString() + ChatColor.BOLD + "Survival-Server"
        );

        // empty line
        objective.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(10);

        // show money
        objective.getScore(ChatColor.AQUA.toString() + ChatColor.BOLD + "Münzen:").setScore(9);

        final Team moneyTeam = scoreboard.registerNewTeam("money");
        moneyTeam.setPrefix(ChatColor.GRAY + "➤ ");
        moneyTeam.setSuffix(
            ChatColor.WHITE.toString() + ChatColor.BOLD + EconomyHandler.getMoney(player)
        );
        moneyTeam.addEntry(ChatColor.BLACK.toString());

        objective.getScore(ChatColor.BLACK.toString()).setScore(8);

        // empty line
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(7);

        // set scoreboard
        player.setScoreboard(scoreboard);
    }

}
