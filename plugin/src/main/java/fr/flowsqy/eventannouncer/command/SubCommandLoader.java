package fr.flowsqy.eventannouncer.command;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class SubCommandLoader {

    @NotNull
    public SubCommand[] load(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig,
            @NotNull RootCommand rootCommand,
            @NotNull Map<String, InformationsData[]> sequences) {
        // Order does not matter but help should be the first for development
        // consistency
        final SubCommand[] subCommands = new SubCommand[3];
        subCommands[0] = new SubCommand("help", new String[] { "h" }, "eventannouncer.command.help",
                messageConfig.getComponentMessage("command.help.help"), new HelpExecutor(subCommands));
        final BaseComponent sendHelpMessage = messageConfig.getComponentMessage("command.help.send");
        subCommands[1] = new SubCommand("send", new String[] { "s" }, "eventannouncer.command.send", sendHelpMessage,
                new SendExecutor(plugin, sequences, messageConfig, sendHelpMessage), new SendTabCompleter(sequences));
        subCommands[2] = new SubCommand("reload", new String[] { "rl" }, "eventannouncer.command.reload",
                messageConfig.getComponentMessage("command.help.reload"),
                new ReloadExecutor(plugin, messageConfig, rootCommand));
        return subCommands;
    }

}
