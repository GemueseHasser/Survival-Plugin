package de.jonas.survival.handler.commands;

import de.jonas.Survival;
import de.jonas.survival.objects.annotations.SurvivalCommand;
import de.jonas.survival.objects.commands.JbCommandExecutor;
import lombok.SneakyThrows;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Mithilfe des {@link CommandHandler} kann eine festgelegte Anzahl an {@link Class Klassen} als Befehle registriert
 * werden. In jeder Klasse können eine nicht festgelegte Anzahl an Methoden sein, die die {@link SurvivalCommand
 * SurvivalCommand-Annotation} haben. Alle Methode, die diese Annotation haben, werden in diesem Handler als Befehl mit
 * den Angaben der Annotation registriert.
 */
@NotNull
public final class CommandHandler {

    //<editor-fold desc="registering">

    /**
     * Registriert eine nicht festgelegte Anzahl an Methoden, mit einer {@link SurvivalCommand
     * SurvivalCommand-Annotaion}, in einer festgelegten Anzahl an Klassen.
     *
     * @param commandClasses Die Klassen, in denen nach den Befehls-Implementationen gesucht wird.
     */
    @SneakyThrows
    public void register(@NotNull final Class<?>[] commandClasses) {
        // get all classes from array
        for (@NotNull final Class<?> clazz : commandClasses) {
            final Object object = clazz.newInstance();
            final Method[] methods = clazz.getDeclaredMethods();
            // get all methods from current class
            for (@NotNull final Method method : methods) {
                // check if current method has annotation
                if (!method.isAnnotationPresent(SurvivalCommand.class)) {
                    continue;
                }

                // declare annotation
                final SurvivalCommand annotation = method.getAnnotation(SurvivalCommand.class);

                // get command information from annotation
                final String command = annotation.command();
                final int min = annotation.minLength();
                final int max = annotation.maxLength();
                final String permission = annotation.permission();
                final String usage = annotation.usage();

                final PluginCommand mainCommand = Survival.getInstance().getCommand(command);

                // declare command-executor
                final JbCommandExecutor commandExecutor = new JbCommandExecutor(
                    method,
                    object,
                    permission,
                    usage,
                    min,
                    max
                );

                // register command
                mainCommand.setExecutor(commandExecutor);
            }
        }
    }
    //</editor-fold>

}
