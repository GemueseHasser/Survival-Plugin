package de.jonas.survival.objects.annotations;

import de.jonas.survival.handler.commands.CommandHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mithilfe einer {@link SurvivalCommand JBCommand-Annotation} wird ein neuer Befehl eingeleitet, welcher in dem {@link
 * CommandHandler} registriert wird.
 */
@NotNull
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SurvivalCommand {

    /**
     * Die Bezeichnung des Befehls.
     *
     * @return Die Bezeichnung des Befehls.
     */
    @NotNull
    String command() default "command";

    /**
     * Die minimale Argumenten-Länge, die der Befehl haben muss.
     *
     * @return Die minimale Argumenten-Länge, die der Befehl haben muss.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int minLength() default 0;

    /**
     * Die maximale Argumenten-Länge, die der Befehl haben muss.
     *
     * @return Die maximale Argumenten-Länge, die der Befehl haben muss.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int maxLength() default 0;

    /**
     * Die Permission, die man haben muss, um den Befehl ausführen zu können.
     *
     * @return Die Permission, die man haben muss, um den Befehl ausführen zu können.
     */
    @NotNull
    String permission() default "survival.default";

    /**
     * Die richtige Benutzung des Befehls.
     *
     * @return Die richtige Benutzung des Befehls.
     */
    @NotNull
    String usage() default "no usage registered!";

}
