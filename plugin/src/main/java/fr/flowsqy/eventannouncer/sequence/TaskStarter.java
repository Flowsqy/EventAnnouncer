package fr.flowsqy.eventannouncer.sequence;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.session.SessionManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class TaskStarter {

    private final Plugin plugin;
    private final InformationsData[] sequence;
    private final SessionManager sessionManager;
    private int current;

    public TaskStarter(@NotNull Plugin plugin, @NotNull InformationsData[] sequence,
            @NotNull SessionManager sessionManager) {
        this.plugin = plugin;
        this.sequence = sequence;
        this.sessionManager = sessionManager;
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
        // Session
        final String session = informationsData.session();
        if (session != null) {
            sessionManager.startSession(session);
        }
        // Title
        final InformationData<TitleData> titleData = informationsData.title();
        if (titleData != null) {
            final Consumer<ProxyServer> nextTask = new SendInformationTask(titleData.servers(),
                    (proxy, player) -> {
                        final TitleData data = titleData.information();
                        final Title title = proxy.createTitle().reset()
                                .stay(data.stay())
                                .fadeIn(data.fadeIn())
                                .fadeOut(data.fadeOut());
                        if (data.title() != null) {
                            title.title(data.title());
                        }
                        if (data.subTitle() != null) {
                            title.subTitle(data.subTitle());
                        }
                        title.send(player);
                    });
            sendTask = sendTask == null ? nextTask : sendTask.andThen(nextTask);
        }

        // ActionBar
        final InformationData<BaseComponent[]> actionBarData = informationsData.actionBar();
        if (actionBarData != null) {
            final Consumer<ProxyServer> nextTask = new SendInformationTask(actionBarData.servers(),
                    (s, player) -> player.sendMessage(ChatMessageType.ACTION_BAR, actionBarData.information()));
            sendTask = sendTask == null ? nextTask : sendTask.andThen(nextTask);
        }

        // Message
        final InformationData<BaseComponent[]> messageData = informationsData.message();
        if (messageData != null) {
            final Consumer<ProxyServer> nextTask = new SendInformationTask(messageData.servers(),
                    (s, player) -> player.sendMessage(ChatMessageType.SYSTEM, messageData.information()));
            sendTask = sendTask == null ? nextTask : sendTask.andThen(nextTask);
        }

        Objects.requireNonNull(sendTask);
        return sendTask;
    }

}
