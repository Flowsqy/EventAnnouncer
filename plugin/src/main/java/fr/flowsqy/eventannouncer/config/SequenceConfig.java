package fr.flowsqy.eventannouncer.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.sequence.InformationData;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SequenceConfig {

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
    public Map<String, InformationsData[]> loadSequences(@NotNull ProxyServer proxyServer, @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry) {
        final Map<String, InformationsData[]> sequences = new HashMap<>();
        for (String key : configuration.getKeys()) {
            final Optional<InformationsData[]> informationSequence = loadSequence(proxyServer, key, logger,
                    messageRegistry);
            if (informationSequence.isEmpty()) {
                continue;
            }
            sequences.put(key, informationSequence.get());
        }
        return sequences;

    }

    @NotNull
    private Optional<InformationsData[]> loadSequence(@NotNull ProxyServer proxyServer, @NotNull String sequenceName,
            @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry) {
        final Configuration sequenceSection = configuration.getSection(sequenceName);
        if (sequenceSection == null) {
            logger.warning("'" + sequenceName + "' is not a section");
            return Optional.empty();
        }

        final List<InformationsData> rawSequence = new LinkedList<>();
        for (String key : sequenceSection.getKeys()) {
            final Optional<InformationsData> informationsData = loadInformations(proxyServer, sequenceName,
                    sequenceSection, key,
                    logger,
                    messageRegistry);
            if (informationsData.isEmpty()) {
                continue;
            }
        }
        if (rawSequence.isEmpty()) {
            logger.warning("Empty sequence '" + sequenceName + "'");
            return Optional.empty();
        }
        return Optional.of(rawSequence.toArray(new InformationsData[0]));
    }

    @NotNull
    private Optional<InformationsData> loadInformations(@NotNull ProxyServer proxyServer, @NotNull String sequenceName,
            @NotNull Configuration sequenceSection, @NotNull String key, @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry) {
        final Configuration informationsSection = sequenceSection.getSection(key);

        if (informationsSection == null) {
            logger.warning("'" + key + "' is not a section in sequence '" + sequenceName + "'");
            return Optional.empty();
        }

        int delay = informationsSection.getInt("delay");
        if (delay < 0) {
            logger.warning("Invalid delay (" + delay + ") in " + sequenceName + "." + key + ". Set it to 0");
            delay = 0;
        }
        final String informationsPath = sequenceName + "." + key;
        final Optional<InformationData<BaseComponent[]>> messageData = loadInformation(proxyServer, informationsPath,
                "message",
                informationsSection, logger, messageRegistry, this::loadComponent);
        final Optional<InformationData<BaseComponent[]>> actionBarData = loadInformation(proxyServer, informationsPath,
                "actionbar",
                informationsSection, logger, messageRegistry, this::loadComponent);
        final Optional<InformationData<Title>> titleData = loadInformation(proxyServer, informationsPath, "title",
                informationsSection, logger, messageRegistry, this::loadTitle);
        if (messageData.isEmpty() && actionBarData.isEmpty() && titleData.isEmpty()) {
            logger.warning("Empty information in " + informationsPath);
            return Optional.empty();
        }
        return Optional.of(new InformationsData(delay, messageData.orElse(null), actionBarData.orElse(null),
                titleData.orElse(null)));
    }

    @NotNull
    private <T> Optional<InformationData<T>> loadInformation(@NotNull ProxyServer proxyServer,
            @NotNull String informationsPath, @NotNull String key,
            @NotNull Configuration informationsSection, @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry, @NotNull SpecificLoader<T> loadSpecifics) {
        final Configuration informationSection = informationsSection.getSection(key);
        if (informationSection == null) {
            return Optional.empty();
        }
        final String informationPath = informationsPath + "." + key;
        final List<String> servers = informationSection.getStringList("servers");
        if (servers.isEmpty()) {
            logger.warning("Empty server list in " + informationPath);
            return Optional.empty();
        }
        final Optional<T> data = loadSpecifics.load(proxyServer, informationPath, informationSection, logger, messageRegistry);
        if (data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new InformationData<T>(servers.toArray(new String[0]), data.get()));
    }

    private interface SpecificLoader<T> {
        Optional<T> load(@NotNull ProxyServer proxyServer, @NotNull String informationPath,
                @NotNull Configuration informationSection, @NotNull Logger logger,
                @NotNull Map<String, BaseComponent[]> messageRegistry);
    }

    @NotNull
    private Optional<BaseComponent[]> loadComponent(@NotNull ProxyServer proxyServer, @NotNull String informationPath,
            @NotNull Configuration informationSection, @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry) {
        return loadFromMessageRegistry(informationPath, informationSection, "id", logger, messageRegistry);
    }

    @NotNull
    private Optional<Title> loadTitle(@NotNull ProxyServer proxyServer, @NotNull String informationPath,
            @NotNull Configuration informationSection, @NotNull Logger logger,
            @NotNull Map<String, BaseComponent[]> messageRegistry) {
        final Optional<BaseComponent[]> titleMessage = loadFromMessageRegistry(informationPath, informationSection,
                "title", logger, messageRegistry);
        final Optional<BaseComponent[]> subTitleMessage = loadFromMessageRegistry(informationPath, informationSection,
                "subtitle", logger, messageRegistry);
        if (titleMessage.isEmpty() && subTitleMessage.isEmpty()) {
            logger.warning("Empty title in " + informationPath);
            return Optional.empty();
        }
        final Title title = proxyServer.createTitle();
        title.title(titleMessage.orElse(null));
        title.subTitle(subTitleMessage.orElse(null));
        title.stay(informationSection.getInt("stay", 60));
        title.fadeIn(informationSection.getInt("fade-in", 20));
        title.fadeOut(informationSection.getInt("fade-out", 20));
        return Optional.of(title);
    }

    @NotNull
    private Optional<BaseComponent[]> loadFromMessageRegistry(@NotNull String sectionPath,
            @NotNull Configuration section, @NotNull String path,
            @NotNull Logger logger, @NotNull Map<String, BaseComponent[]> messageRegistry) {
        final String id = section.getString(path);
        if (id == null) {
            return Optional.empty();
        }
        final BaseComponent[] components = messageRegistry.get(id);
        if (components == null) {
            logger.warning("'" + id + "' is not a registered message in " + sectionPath);
            return Optional.empty();
        }
        return Optional.of(components);
    }
}
