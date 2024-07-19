package fr.flowsqy.eventannouncer.sequence;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class TaskStarter {

    private final Plugin plugin;
    private final InformationsData[] sequence;
    private int current;

    public TaskStarter(Plugin plugin, InformationsData[] sequence) {
        this.plugin = plugin;
        this.sequence = sequence;
        this.current = 0;
    }

    public void launchNext() {
        if (current >= sequence.length) {
            return;
        }

        final InformationsData informationsData = sequence[current];
        final Consumer<ProxyServer> sendTask = getSendTask(informationsData);

        plugin.getProxy().getScheduler().schedule(
                plugin,
                new SendInformationsTask(sendTask, this::launchNext),
                informationsData.delay(), TimeUnit.MILLISECONDS);

        current++;
    }

    private Consumer<ProxyServer> getSendTask(@NotNull InformationsData informationsData) {
        Consumer<ProxyServer> sendTask = null;
        // Title
        final InformationData<Title> titleData = informationsData.title();
        if (titleData != null) {
            sendTask = new SendInformationTask(titleData.servers(),
                    player -> player.sendTitle(titleData.information()));
        }

        // ActionBar
        final InformationData<BaseComponent[]> actionBarData = informationsData.actionBar();
        if (actionBarData != null) {
            final Consumer<ProxyServer> nextTask = new SendInformationTask(actionBarData.servers(),
                    player -> player.sendMessage(ChatMessageType.ACTION_BAR, actionBarData.information()));
            sendTask = sendTask == null ? nextTask : sendTask.andThen(nextTask);
        }

        // Message
        final InformationData<BaseComponent[]> messageData = informationsData.message();
        if (messageData != null) {
            final Consumer<ProxyServer> nextTask = new SendInformationTask(messageData.servers(),
                    player -> player.sendMessage(ChatMessageType.SYSTEM, messageData.information()));
            sendTask = sendTask == null ? nextTask : sendTask.andThen(nextTask);
        }

        Objects.requireNonNull(sendTask);
        return sendTask;
    }

}
