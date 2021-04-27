package de.jonas.survival.task;

import de.jonas.survival.handler.scoreboard.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class ScoreboardTask extends BukkitRunnable {

    @Override
    public void run() {
        for (@NotNull final Player all : Bukkit.getOnlinePlayers()) {
            ScoreboardHandler.setScoreboard(all);
        }
    }

}
