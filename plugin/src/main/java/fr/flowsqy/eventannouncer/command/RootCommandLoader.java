package fr.flowsqy.eventannouncer.command;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.ConfigLoader;
import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.config.MessageRegistryLoader;
import fr.flowsqy.eventannouncer.config.SequenceConfig;
import fr.flowsqy.eventannouncer.config.SessionConfig;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import fr.flowsqy.eventannouncer.session.SessionData;
import fr.flowsqy.eventannouncer.session.SessionManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class RootCommandLoader {

    public void load(@NotNull Plugin plugin, @NotNull RootCommand rootCommand) {
        final ConfigLoader configLoader = new ConfigLoader();
        final MessageConfig messageConfig = new MessageConfig();
        messageConfig.load(configLoader, plugin, "messages.yml");
        messageConfig.loadPrefix();
        final MessageRegistryLoader messageRegistryLoader = new MessageRegistryLoader();
        messageRegistryLoader.load(configLoader, plugin, "message-registry.lang");
        final SequenceConfig sequencesConfig = new SequenceConfig();
        sequencesConfig.load(configLoader, plugin, "sequences.yml");
        final Map<String, BaseComponent[]> messageRegistry = messageRegistryLoader.getMessages(plugin.getLogger());
        final SessionConfig sessionConfig = new SessionConfig();
        sessionConfig.load(configLoader, plugin, "sessions.yml");
        final Map<String, SessionData> sessions = sessionConfig.loadSessions(plugin.getLogger());
        final SessionManager sessionManager = new SessionManager(plugin, sessions);
        final Map<String, InformationsData[]> sequences = sequencesConfig.loadSequences(plugin.getLogger(),
                messageRegistry, sessions.keySet());
        final SubCommandLoader subCommandLoader = new SubCommandLoader();
        final SubCommand[] subCommands = subCommandLoader.load(plugin, messageConfig, rootCommand, sequences,
                sessionManager);
        rootCommand.load(subCommands, messageConfig.getMessage("command.dont-have-permission"),
                subCommands[0].getExecutor());
    }

}
