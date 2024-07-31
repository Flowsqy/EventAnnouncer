package fr.flowsqy.eventannouncer.session;

import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Session {

    private final Plugin plugin;
    private final ServerInfo destinationServer;
    private final TeleportDelayer teleportDelayer;
    private final ScheduledTask task;

    public void requestTeleport(@NotNull ProxiedPlayer proxiedPlayer) {
        if (destinationServer == null) {
            proxiedPlayer.sendMessage(serverDownMessage);
            return;
        }
        if (!destinationServer.canAccess(proxiedPlayer)) {
            proxiedPlayer.sendMessage(cantAccessMessage));
            return;
        }
        if (proxiedPlayer.getServer().getInfo().equals(destinationServer)) {
            proxiedPlayer.sendMessage(alreadyConnected);
            return;
        }
        if (teleportDelayer.contains(proxiedPlayer)) {
            proxiedPlayer.sendMessage(alreadyInQueue);
            return;
        }
        teleportDelayer.subscribe(proxiedPlayer);
        proxiedPlayer.sendMessage(successMessage);
    }    

    public void close() {
        task.cancel();
    }

}
