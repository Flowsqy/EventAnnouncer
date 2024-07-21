package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.TabExecutor;

public class SubCommand {

    private final String name;
    private final String[] aliases;
    private final String permission;
    private final BaseComponent helpMessage;
    private final Executor executor;
    private final TabExecutor tabCompleter;

    public SubCommand(@NotNull String name, @NotNull String[] aliases, @NotNull String permission,
            @NotNull BaseComponent helpMessage, @NotNull Executor executor) {
        this(name, aliases, permission, helpMessage, executor, null);
    }

    public SubCommand(@NotNull String name, @NotNull String[] aliases, @NotNull String permission,
            @NotNull BaseComponent helpMessage, @NotNull Executor executor, @Nullable TabExecutor tabCompleter) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.helpMessage = helpMessage;
        this.executor = executor;
        this.tabCompleter = tabCompleter;
    }

    public boolean match(String argument) {
        if (name.equalsIgnoreCase(argument)) {
            return true;
        }
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(argument)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public BaseComponent getHelpMessage() {
        return helpMessage;
    }

    public Executor getExecutor() {
        return executor;
    }

    public TabExecutor getTabCompleter() {
        return tabCompleter;
    }

}
