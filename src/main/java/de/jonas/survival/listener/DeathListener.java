package de.jonas.survival.listener;

import de.jonas.Survival;
import de.jonas.survival.commands.PlayerCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public final class DeathListener implements Listener {

    @EventHandler
    public void onDeath(@NotNull final PlayerDeathEvent e) {
        e.setDeathMessage(Survival.getPrefix() + e.getEntity().getName() + " ist verreckt!");
        PlayerCommands.DEATH_LOCATIONS.put(e.getEntity().getUniqueId(), e.getEntity().getLocation());
    }

}
