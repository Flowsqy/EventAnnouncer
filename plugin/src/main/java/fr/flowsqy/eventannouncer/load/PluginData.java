package fr.flowsqy.eventannouncer.load;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.sequence.InformationsData;
import fr.flowsqy.eventannouncer.session.SessionManager;

public record PluginData(@NotNull MessageConfig messageConfig, @NotNull Map<String, InformationsData[]> sequences,
        @NotNull SessionManager sessionManager) {
}
