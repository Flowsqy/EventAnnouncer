package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;

public interface Executor {

    void execute(@NotNull CommandSender sender, @NotNull String[] args);

}
