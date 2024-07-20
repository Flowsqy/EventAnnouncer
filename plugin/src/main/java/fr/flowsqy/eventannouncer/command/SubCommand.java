package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

public class SubCommand {

    private final String name;
    private final String[] aliases;
    private final String permission;
    private final Executor executor;

    public SubCommand(@NotNull String name, @NotNull String[] aliases, @NotNull String permission,
            @NotNull Executor executor) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
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

    public Executor getExecutor() {
        return executor;
    }

}
