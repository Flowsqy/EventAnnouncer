package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.load.PluginData;
import fr.flowsqy.eventannouncer.session.RequestTeleportListener;
import net.md_5.bungee.api.plugin.Plugin;

public class RootCommandLoader {

    public void load(@NotNull Plugin plugin, @NotNull PluginData pluginData, @NotNull RootCommand rootCommand,
            @NotNull RequestTeleportListener requestTeleportListner) {
        final SubCommandLoader subCommandLoader = new SubCommandLoader();
        final SubCommand[] subCommands = subCommandLoader.load(plugin, pluginData.messageConfig(), rootCommand,
                requestTeleportListner, pluginData.sequences(), pluginData.sessionManager());
        rootCommand.load(subCommands, pluginData.messageConfig().getMessage("command.dont-have-permission"),
                subCommands[0].getExecutor());
    }

}
