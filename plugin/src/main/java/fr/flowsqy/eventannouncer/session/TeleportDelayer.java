package fr.flowsqy.eventannouncer.session;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class TeleportDelayer {

    private final Plugin plugin;
    private final ServerInfo destinationServer;
    private final Queue<ProxiedPlayer> queue;
    private final int playerByIteration, period;
    private final Lock lock;
    private ScheduledTask teleportTask;

    // TODO Make it thread safe

    public TeleportDelayer(@NotNull Plugin plugin, @NotNull ServerInfo destinationServer, int playerByIteration,
            int period) {
        this.plugin = plugin;
        this.destinationServer = destinationServer;
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
                final ServerConnectRequest connectRequest = ServerConnectRequest.builder()
                        .target(destinationServer)
                        .build();
                for (int i = 0; i < playerByIteration && !queue.isEmpty(); i++) {
                    final ProxiedPlayer player = queue.poll();
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
