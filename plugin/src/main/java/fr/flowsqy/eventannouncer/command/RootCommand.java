package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class RootCommand extends Command {

    private final static String NAME = "eventannouncer";
    private final static String PERMISSION = "eventannouncer.command";
    private final static String[] ALIASES = new String[] { "ea" };

    private final RootCommandManager rootCommandManager;

    public RootCommand(@NotNull RootCommandManager rootCommandManager, @NotNull MessageConfig messageConfig) {
        super(NAME, PERMISSION, ALIASES);
        this.rootCommandManager = rootCommandManager;
        setPermissionMessage(messageConfig.getMessage("command.dont-have-permission"));
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 0) {
            // Send help
            return;
        }
        final String argument = args[0];
        for (SubCommand subCommand : rootCommandManager.getSubCommands()) {
            if (!subCommand.match(argument)) {
                continue;
            }
            if (sender.hasPermission(subCommand.getPermission())) {
                subCommand.getExecutor().execute(sender, args);
            }
            break;
        }
        // Send help
    }

}
