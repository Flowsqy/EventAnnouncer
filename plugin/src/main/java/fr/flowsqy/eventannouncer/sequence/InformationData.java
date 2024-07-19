package fr.flowsqy.eventannouncer.sequence;

import org.jetbrains.annotations.NotNull;

public record InformationData<T>(@NotNull String[] servers, @NotNull T information) {
}
