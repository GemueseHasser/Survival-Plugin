package de.jonas.survival.objects.commands;

import de.jonas.Survival;
import lombok.SneakyThrows;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Method;

/**
 * Mithilfe des {@link JbCommandExecutor} wird ein neuer Befehl implementiert, bzw. das was ausgeführt wird, sobald der
 * Befehl eingegeben wurde, bzw. die Basis-Abfragen des Befehls.
 */
public final class JbCommandExecutor implements CommandExecutor {

    //<editor-fold desc="LOCAL FIELDS">
    /** Die {@link Method Methode}, die aufgerufen wird, sobald alle Basis-Überprüfungen abgeschlossen sind. */
    private final Method method;
    /** Die Instanz, der Klasse, in der die Methode steht. */
    private final Object instance;
    /** Der Permissions, die für den Befehl nötig sind. */
    private final String permission;
    /** Die korrekte Benutzung des Befehls. */
    private final String usage;
    /** Die minimale Argumenten-Länge, die der Befehl haben muss. */
    private final int minLength;
    /** Die maximale Argumenten-Länge, die der Befehl haben muss. */
    private final int maxLength;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue und vollständig unabhängige Instanz des {@link JbCommandExecutor}. Mithilfe des {@link
     * JbCommandExecutor} wird ein neuer Befehl implementiert, bzw. das was ausgeführt wird, sobald der Befehl
     * eingegeben wurde, bzw. die Basis-Abfragen des Befehls.
     *
     * @param method     Die {@link Method Methode}, die aufgerufen wird, sobald alle Basis-Überprüfungen abgeschlossen
     *                   sind.
     * @param instance   Die Instanz, der Klasse, in der die Methode steht.
     * @param permission Der Permissions, die für den Befehl nötig sind.
     * @param usage      Die korrekte Benutzung des Befehls.
     * @param minLength  Die minimale Argumenten-Länge, die der Befehl haben muss.
     * @param maxLength  Die maximale Argumenten-Länge, die der Befehl haben muss.
     */
    public JbCommandExecutor(
        @NotNull final Method method,
        @NotNull final Object instance,
        @NotNull final String permission,
        @NotNull final String usage,
        @Range(from = 0, to = Integer.MAX_VALUE) final int minLength,
        @Range(from = 0, to = Integer.MAX_VALUE) final int maxLength
    ) {
        this.method = method;
        this.instance = instance;
        this.permission = permission;
        this.usage = usage;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
    //</editor-fold>


    //<editor-fold desc="implementation">
    @Override
    @SneakyThrows
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final org.bukkit.command.Command command,
        @NotNull final String label,
        @NotNull final String[] args
    ) {
        // check if command-sender is instanceof player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Survival.getPrefix() + "Du musst ein Spieler sein!");
            return true;
        }

        // declare player
        final Player player = (Player) sender;

        // check if player has permissions
        if (!player.hasPermission(this.permission)) {
            player.sendMessage(Survival.getPrefix() + "Dazu hast du keine Rechte!");
            return true;
        }

        // check if command-length is correct
        if (!(args.length >= this.minLength && args.length <= maxLength)) {
            player.sendMessage(Survival.getPrefix() + "Bitte benutze " + this.usage);
            return true;
        }

        // invoke activity-method
        method.invoke(this.instance, player, args);
        return true;
    }
    //</editor-fold>
}
