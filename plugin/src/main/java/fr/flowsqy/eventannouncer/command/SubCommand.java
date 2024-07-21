package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;

public class SubCommand {

    private final String name;
    private final String[] aliases;
    private final String permission;
    private final BaseComponent helpMessage;
    private final Executor executor;

    public SubCommand(@NotNull String name, @NotNull String[] aliases, @NotNull String permission, BaseComponent helpMessage,
            @NotNull Executor executor) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.helpMessage = helpMessage;
        this.executor = executor;
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

}
