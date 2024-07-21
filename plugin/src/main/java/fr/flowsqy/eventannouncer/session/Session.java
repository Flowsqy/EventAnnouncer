package fr.flowsqy.eventannouncer.session;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Session {

    private final Plugin plugin;
    private final Queue<ProxiedPlayer> teleportQueue;
    private ScheduledTask task = null;

    public void subscribe(@NotNull ProxiedPlayer player) {
        teleportQueue.offer(player);
        checkTask();
    }
    
    private void checkTask() {
        if (task != null) {
            return;
        }
        task = plugin.getProxy().getScheduler().schedule(plugin, () -> {
            
        }, delay, period, TimeUnit.MILLISECONDS);
    }

}
