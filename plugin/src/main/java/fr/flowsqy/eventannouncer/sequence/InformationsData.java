package fr.flowsqy.eventannouncer.sequence;

import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;

public record InformationsData(
        int delay,
        @Nullable String session,
        @Nullable InformationData<BaseComponent[]> message,
        @Nullable InformationData<BaseComponent[]> actionBar,
        @Nullable InformationData<TitleData> title) {
}
