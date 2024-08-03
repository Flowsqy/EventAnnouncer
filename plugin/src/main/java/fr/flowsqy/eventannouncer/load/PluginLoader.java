package fr.flowsqy.eventannouncer.load;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

public class PluginLoader {

    private final static String MESSAGE_FILENAME = "messages.yml";
    private final static String MESSAGE_REGISTRY_FILENAME = "message-registry.lang";
    private final static String SEQUENCE_FILENAME = "sequences.yml";
    private final static String SESSION_FILENAME = "sessions.yml";

    @Nullable
    public PluginData load(@NotNull Plugin plugin) {
        final ConfigLoader configLoader = new ConfigLoader();
        final Logger logger = Objects.requireNonNull(plugin.getLogger());
        final File dataFolder = plugin.getDataFolder();
        if (!configLoader.checkDataFolder(dataFolder)) {
            logger.log(Level.WARNING, "Can not write in the directory : " + dataFolder.getAbsolutePath());
            logger.log(Level.WARNING, "Disable the plugin");
            return null;
        }

        // Messages
        final MessageConfig messageConfig = new MessageConfig();
        messageConfig.load(configLoader, plugin, MESSAGE_FILENAME);
        messageConfig.loadPrefix();
        // Message Registry
        final MessageRegistryLoader messageRegistryLoader = new MessageRegistryLoader();
        messageRegistryLoader.load(configLoader, plugin, MESSAGE_REGISTRY_FILENAME);
        final Map<String, BaseComponent[]> messageRegistry = messageRegistryLoader.getMessages(logger);
        // Sessions
        final SessionConfig sessionConfig = new SessionConfig();
        sessionConfig.load(configLoader, plugin, SESSION_FILENAME);
        // Sequences
        final SequenceConfig sequencesConfig = new SequenceConfig();
        sequencesConfig.load(configLoader, plugin, SEQUENCE_FILENAME);
        final Map<String, SessionData> sessions = sessionConfig.loadSessions(logger);
        final SessionManager sessionManager = new SessionManager(plugin, sessions);
        final Map<String, InformationsData[]> sequences = sequencesConfig.loadSequences(logger,
                messageRegistry, sessionManager);
        return new PluginData(messageConfig, sequences, sessionManager);
    }

}
