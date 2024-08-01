package fr.flowsqy.eventannouncer.session;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Session {

    private final Plugin plugin;
    private final SessionData sessionData;
    private final TeleportDelayer teleportDelayer;

    public Session(@NotNull Plugin plugin, @NotNull SessionData sessionData) {
        this.plugin = plugin;
        this.sessionData = sessionData;
        teleportDelayer = new TeleportDelayer(plugin, sessionData.destinationServer(), sessionData.serverDownMessage(),
                sessionData.playerByIteration(), sessionData.period());
    }

    public void requestTeleport(@NotNull ProxiedPlayer proxiedPlayer) {
        final ServerInfo destinationServer = plugin.getProxy().getServerInfo(sessionData.destinationServer());
        if (destinationServer == null) {
            proxiedPlayer.sendMessage(sessionData.serverDownMessage());
            return;
        }
        if (!destinationServer.canAccess(proxiedPlayer)) {
            proxiedPlayer.sendMessage(sessionData.cantConnectMessage());
            return;
        }
        if (proxiedPlayer.getServer().getInfo().equals(destinationServer)) {
            proxiedPlayer.sendMessage(sessionData.alreadyConnectedMessage());
            return;
        }
        if (teleportDelayer.isSubscribed(proxiedPlayer)) {
            proxiedPlayer.sendMessage(sessionData.alreadyInQueueMessage());
            return;
        }
        teleportDelayer.subscribe(proxiedPlayer);
        proxiedPlayer.sendMessage(sessionData.successMessage());
    }

}
