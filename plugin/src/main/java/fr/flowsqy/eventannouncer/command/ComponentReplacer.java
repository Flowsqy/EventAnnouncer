package fr.flowsqy.eventannouncer.command;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentReplacer {

    @NotNull
    public static BaseComponent replace(@NotNull BaseComponent component, @NotNull String placeholder,
            @NotNull String value) {
        return replace(component, Pattern.compile(placeholder), value);
    }

    @NotNull
    public static BaseComponent replace(@NotNull BaseComponent component, @NotNull Pattern pattern,
            @NotNull String value) {
        final List<BaseComponent> extras = new LinkedList<>();
        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                extras.add(replace(extra, pattern, value));
            }
        }
        String newText = null;
        if (component instanceof TextComponent textComponent) {
            final Matcher matcher = pattern.matcher(textComponent.getText());
            if (matcher.find()) {
                newText = matcher.replaceAll(value);
            }
        }
        if (newText == null && extras.isEmpty()) {
            return component;
        }
        final BaseComponent newComponent = component.duplicate();
        if (newText != null) {
            ((TextComponent) newComponent).setText(newText);
        }
        if (!extras.isEmpty()) {
            newComponent.setExtra(extras);
        }
        return newComponent;
    }

}
