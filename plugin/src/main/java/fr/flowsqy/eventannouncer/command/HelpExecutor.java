package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;

public class HelpExecutor implements Executor {

    private final SubCommand[] subCommands;

    public HelpExecutor(@NotNull SubCommand[] subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        for (SubCommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(subCommand.getHelpMessage());
            }
        }
    }

}
