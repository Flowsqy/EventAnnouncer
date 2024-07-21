package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class RootCommand extends Command {

    private final static String NAME = "eventannouncer";
    private final static String PERMISSION = "eventannouncer.command";
    private final static String[] ALIASES = new String[] { "ea" };

    private SubCommand[] subCommands;
    private Executor helpExecutor;

    public RootCommand() {
        super(NAME, PERMISSION, ALIASES);
        subCommands = null;
        helpExecutor = null;
    }

    public void load(@NotNull SubCommand[] subCommands, @NotNull String permMessage,
            @NotNull Executor helpExecutor) {
        this.subCommands = subCommands;
        this.helpExecutor = helpExecutor;
        setPermissionMessage(permMessage);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 0) {
            helpExecutor.execute(sender, args);
            return;
        }
        final String argument = args[0];
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.match(argument)) {
                continue;
            }
            if (sender.hasPermission(subCommand.getPermission())) {
                subCommand.getExecutor().execute(sender, args);
                return;
            }
            break;
        }
        helpExecutor.execute(sender, args);
    }

}
