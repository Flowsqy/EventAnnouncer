package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommandLoader {

    public void load(@NotNull Plugin plugin) {
        final PluginManager pluginManager = plugin.getProxy().getPluginManager();
        final RootCommand rootCommand = new RootCommand();
        pluginManager.registerCommand(plugin, rootCommand);
        final RootCommandLoader rootCommandLoader = new RootCommandLoader();
        rootCommandLoader.load(plugin, rootCommand);
    }

}
