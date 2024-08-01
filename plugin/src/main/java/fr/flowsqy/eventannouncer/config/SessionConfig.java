package fr.flowsqy.eventannouncer.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.session.SessionData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SessionConfig {

    private Configuration configuration;

    public void load(@NotNull ConfigLoader configLoader, @NotNull Plugin plugin, @NotNull String fileName) {
        final File configFile = configLoader.initFile(plugin.getDataFolder(),
                Objects.requireNonNull(plugin.getResourceAsStream(fileName)), fileName);
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Map<String, SessionData> loadSessions(@NotNull Logger logger) {
        final Map<String, SessionData> sessions = new HashMap<>();
        for (String key : configuration.getKeys()) {
            final Optional<SessionData> sessionData = loadSessionData(logger, key);
            if (sessionData.isEmpty()) {
                continue;
            }
            sessions.put(key, sessionData.get());
        }
        return sessions;
    }

    @NotNull
    private Optional<SessionData> loadSessionData(@NotNull Logger logger, @NotNull String key) {
        final Configuration sessionSection = configuration.getSection(key);
        final String destination = sessionSection.getString("destination", null);
        if (destination == null) {
            logger.warning("No destination specified for '" + key + "'");
            return Optional.empty();
        }
        final int duration = sessionSection.getInt("duration");
        if (duration < 50) {
            logger.warning("Invalid duration specified. Should be >= 50");
            return Optional.empty();
        }
        final int playerByIteration = Math.max(1, sessionSection.getInt("player-by-iteration"));
        final int period = Math.max(0, sessionSection.getInt("period"));
        final BaseComponent serverDownMessage = TextComponent
                .fromLegacy(ChatColor.translateAlternateColorCodes('&', sessionSection.getString("server-down", "")));
        final BaseComponent cantConnectMessage = TextComponent
                .fromLegacy(ChatColor.translateAlternateColorCodes('&', sessionSection.getString("cant-connect", "")));
        final BaseComponent alreadyConnectedMessage = TextComponent.fromLegacy(
                ChatColor.translateAlternateColorCodes('&', sessionSection.getString("already-connected", "")));
        final BaseComponent alreadyInQueueMessage = TextComponent.fromLegacy(
                ChatColor.translateAlternateColorCodes('&', sessionSection.getString("already-in-queue", "")));
        final BaseComponent successMessage = TextComponent
                .fromLegacy(ChatColor.translateAlternateColorCodes('&', sessionSection.getString("success", "")));
        final SessionData sessionData = new SessionData(destination, duration, playerByIteration, period,
                serverDownMessage, cantConnectMessage, alreadyConnectedMessage, alreadyInQueueMessage, successMessage);
        return Optional.of(sessionData);
    }
}
