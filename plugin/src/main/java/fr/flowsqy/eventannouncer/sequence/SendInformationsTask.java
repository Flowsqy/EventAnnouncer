package fr.flowsqy.eventannouncer.sequence;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ProxyServer;

public class SendInformationsTask implements Runnable {

    private final Consumer<ProxyServer> informationSender;
    private final Runnable callback;

    public SendInformationsTask(@NotNull Consumer<ProxyServer> informationSender, @Nullable Runnable callback) {
        this.informationSender = informationSender;
        this.callback = callback;
    }

    @Override
    public void run() {
        informationSender.accept(ProxyServer.getInstance());
        if (callback != null) {
            callback.run();
        }
    }

}
