package fr.flowsqy.eventannouncer.session;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class TeleportDelayer {

    private final Plugin plugin;
    private final String destinationServerName;
    private final Queue<ProxiedPlayer> queue;
    private final BaseComponent serverDownMessage;
    private final int playerByIteration, period;
    private final Lock lock;
    private ScheduledTask teleportTask;

    public TeleportDelayer(@NotNull Plugin plugin, @NotNull String destinationServerName,
            @NotNull BaseComponent serverDownMessage, int playerByIteration,
            int period) {
        this.plugin = plugin;
        this.destinationServerName = destinationServerName;
        this.serverDownMessage = serverDownMessage;
        this.playerByIteration = playerByIteration;
        this.period = period;
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        teleportTask = null;
    }

    public void subscribe(@NotNull ProxiedPlayer player) {
        lock.lock();
        try {
            queue.offer(player);
            checkTask();
        } finally {
            lock.unlock();
        }
    }

    public boolean isSubscribed(@NotNull ProxiedPlayer player) {
        lock.lock();
        try {
            return queue.contains(player);
        } finally {
            lock.unlock();
        }
    }

    private void checkTask() {
        if (teleportTask != null) {
            return;
        }
        teleportTask = plugin.getProxy().getScheduler().schedule(plugin, new TeleportTask(), 0L, period,
                TimeUnit.MILLISECONDS);
    }

    private class TeleportTask implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                if (queue.isEmpty()) {
                    teleportTask.cancel();
                    teleportTask = null;
                    return;
                }
                final ServerInfo destinationServer = plugin.getProxy().getServerInfo(destinationServerName);
                final ServerConnectRequest connectRequest = destinationServer == null ? null
                        : ServerConnectRequest.builder()
                                .target(destinationServer)
                                .reason(ServerConnectEvent.Reason.PLUGIN)
                                .build();
                for (int i = 0; i < playerByIteration && !queue.isEmpty(); i++) {
                    final ProxiedPlayer player = queue.poll();
                    if (destinationServer == null) {
                        player.sendMessage(serverDownMessage);
                        continue;
                    }
                    if (player.getServer().getInfo().equals(destinationServer)) {
                        i--;
                        continue;
                    }
                    player.connect(connectRequest);
                }
            } finally {
                lock.unlock();
            }
        }

    }

}
