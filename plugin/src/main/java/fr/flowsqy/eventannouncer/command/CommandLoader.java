package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.load.PluginData;
import fr.flowsqy.eventannouncer.session.RequestTeleportListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommandLoader {

    public void load(@NotNull Plugin plugin, @NotNull PluginData pluginData,
            @NotNull RequestTeleportListener requestTeleportListener) {
        final PluginManager pluginManager = plugin.getProxy().getPluginManager();
        final RootCommand rootCommand = new RootCommand();
        pluginManager.registerCommand(plugin, rootCommand);
        final RootCommandLoader rootCommandLoader = new RootCommandLoader();
        rootCommandLoader.load(plugin, pluginData, rootCommand, requestTeleportListener);
    }

}
