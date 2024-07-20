package fr.flowsqy.eventannouncer.command;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import net.md_5.bungee.api.plugin.Plugin;

public class RootCommandManager {

    private SubCommand helpCommand;
    private SubCommand[] subCommands;

    public void load(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig,
            @NotNull Map<String, InformationsData[]> sequences) {
        helpCommand = new SubCommand("help", new String[] { "h" }, "eventannouncer.command.help", new HelpExecutor());

        subCommands = new SubCommand[] {
                helpCommand,
                new SubCommand("send", new String[] { "s" }, "eventannouncer.command.send",
                        new SendExecutor(plugin, sequences, messageConfig)),
        };
    }

    public SubCommand getHelpCommand() {
        return helpCommand;
    }

    public SubCommand[] getSubCommands() {
        return subCommands;
    }

}
