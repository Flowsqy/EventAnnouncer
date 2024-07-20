package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.ConfigLoader;
import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.config.MessageRegistryLoader;
import fr.flowsqy.eventannouncer.config.SequenceConfig;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommandLoader {

    public void load(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig) {
        final PluginManager pluginManager = plugin.getProxy().getPluginManager();
        final RootCommandManager rootCommandManager = new RootCommandManager();
        final ConfigLoader configLoader = new ConfigLoader();
        final MessageRegistryLoader messageRegistryLoader = new MessageRegistryLoader();
        messageRegistryLoader.load(configLoader, plugin, "message-registry.lang");
        final SequenceConfig sequencesConfig = new SequenceConfig();
        sequencesConfig.load(configLoader, plugin, "sequences.yml");
        rootCommandManager.load(plugin, messageConfig, sequencesConfig.loadSequences(plugin.getLogger(),
                messageRegistryLoader.getMessages(plugin.getLogger())));
        pluginManager.registerCommand(plugin, new RootCommand(rootCommandManager, messageConfig));
    }

}
