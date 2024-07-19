package fr.flowsqy.eventannouncer.sequence;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendInformationTask implements Consumer<ProxyServer> {

    private final String[] servers;
    private final Consumer<ProxiedPlayer> sendTask;

    public SendInformationTask(@NotNull String[] servers, @NotNull Consumer<ProxiedPlayer> sendTask) {
        this.servers = servers;
        this.sendTask = sendTask;
    }

    @Override
    public void accept(ProxyServer proxyServer) {
        for (String server : servers) {
            final ServerInfo serverInfo = proxyServer.getServerInfo(server);
            if (serverInfo == null) {
                continue;
            }
            for (ProxiedPlayer player : serverInfo.getPlayers()) {
                sendTask.accept(player);
            }
        }
    }

}
