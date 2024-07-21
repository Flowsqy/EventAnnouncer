package fr.flowsqy.eventannouncer.command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class RootCommand extends Command implements TabExecutor {

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

    @Override
    @NotNull
    public Iterable<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            final List<String> completions = new LinkedList<>();
            final String arg = args[0].toLowerCase(Locale.ENGLISH);
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().startsWith(arg) && sender.hasPermission(subCommand.getPermission())) {
                    completions.add(subCommand.getName());
                }
            }
            return completions;
        }
        final String argument = args[0];
        for (SubCommand subCommand : subCommands) {
            if (!subCommand.match(argument)) {
                continue;
            }
            if (!sender.hasPermission(subCommand.getPermission())) {
                break;
            }
            final TabExecutor tabCompleter = subCommand.getTabCompleter();
            if (tabCompleter == null) {
                return Collections.emptyList();
            }
            return tabCompleter.onTabComplete(sender, args);
        }
        return Collections.emptyList();
    }

}
