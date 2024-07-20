package fr.flowsqy.eventannouncer.command;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import fr.flowsqy.eventannouncer.sequence.TaskStarter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class SendExecutor implements Executor {

    private final Plugin plugin;
    private final Map<String, InformationsData[]> sequences;
    private final BaseComponent successMessage, failMessage;

    public SendExecutor(@NotNull Plugin plugin, @NotNull Map<String, InformationsData[]> sequences,
            @NotNull MessageConfig messageConfig) {
        this.plugin = plugin;
        this.sequences = sequences;
        successMessage = messageConfig.getComponentMessage("command.send.success");
        failMessage = messageConfig.getComponentMessage("command.send.fail");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 2) {
            // TODO Send help
            return;
        }
        final String sequenceName = args[1];
        final InformationsData[] sequence = sequences.get(sequenceName);
        if (sequence == null) {
            sender.sendMessage(ComponentReplacer.replace(failMessage, "%sequence%", sequenceName));
            return;
        }
        final TaskStarter taskStarter = new TaskStarter(plugin, sequence);
        taskStarter.launchNext();
        sender.sendMessage(ComponentReplacer.replace(successMessage, "%sequence%", sequenceName));
    }

}
