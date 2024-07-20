package fr.flowsqy.eventannouncer.sequence;

import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;

public record TitleData(@Nullable BaseComponent[] title, @Nullable BaseComponent[] subTitle, int stay, int fadeIn,
        int fadeOut) {
}
