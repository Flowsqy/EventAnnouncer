package fr.flowsqy.eventannouncer.config;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MessageConfig {

    private Configuration configuration;
    private String prefix;

    public void load(@NotNull ConfigLoader configLoader, @NotNull Plugin plugin, @NotNull String fileName) {
        final File configFile = configLoader.initFile(plugin.getDataFolder(), Objects.requireNonNull(plugin.getResourceAsStream(fileName)), fileName);
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private String formatColor(@Nullable String message) {
        return message == null ? null : ChatColor.translateAlternateColorCodes('&', message);
    }

    public void loadPrefix() {
        prefix = formatColor(configuration.getString("prefix"));
    }

    @Nullable
    public String getMessage(@NotNull String path) {
        final String message = formatColor(configuration.getString(path));
        if (message == null) {
            return null;
        }
        return prefix == null ? message : message.replace("%prefix%", prefix);
    }

    @Nullable
    public BaseComponent getComponentMessage(@NotNull String path) {
        final String message = getMessage(path);
        if (message == null) {
            return null;
        }
        return TextComponent.fromLegacy(message);
    }

}
