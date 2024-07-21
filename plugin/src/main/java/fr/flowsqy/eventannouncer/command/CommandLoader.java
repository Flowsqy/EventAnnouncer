package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommandLoader {

    public void load(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig) {
        final PluginManager pluginManager = plugin.getProxy().getPluginManager();
        final RootCommand rootCommand = new RootCommand();
        pluginManager.registerCommand(plugin, new RootCommand());
        final RootCommandLoader rootCommandLoader = new RootCommandLoader();
        rootCommandLoader.load(plugin, rootCommand);
    }

}
