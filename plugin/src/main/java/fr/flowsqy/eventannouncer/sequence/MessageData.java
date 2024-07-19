package fr.flowsqy.eventannouncer.sequence;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;

public record MessageData(@NotNull String[] servers, @NotNull BaseComponent[] message) {
}
