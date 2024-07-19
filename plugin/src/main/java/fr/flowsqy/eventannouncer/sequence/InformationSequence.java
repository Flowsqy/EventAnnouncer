package fr.flowsqy.eventannouncer.sequence;

import org.jetbrains.annotations.NotNull;

public record InformationSequence(@NotNull int[] timings, @NotNull InformationsData[] informations) {

}
