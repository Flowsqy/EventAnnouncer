package fr.flowsqy.eventannouncer.session;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;

public record SessionData(@NotNull String destinationServer, int duration, int playerByIteration, int period,
        @NotNull BaseComponent[] serverDownMessage, @NotNull BaseComponent[] cantConnectMessage,
        @NotNull BaseComponent[] alearyConnectedMessage, @NotNull BaseComponent[] alreadyInQueueMessage,
        @NotNull BaseComponent[] successMessage) {
}
