package fr.flowsqy.eventannouncer.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class SessionManager {

    private final Plugin plugin;
    private final Map<String, ActiveSession> activeSessions;
    private final Map<String, SessionData> registeredSession;
    private final Lock lock;

    public SessionManager(@NotNull Plugin plugin, @NotNull Map<String, SessionData> sessions) {
        this.plugin = plugin;
        this.registeredSession = sessions;
        activeSessions = new HashMap<>();
        lock = new ReentrantLock();
    }


    @Nullable
    public Session getSession(@NotNull String sessionName) {
        final ActiveSession session = activeSessions.get(sessionName);
        return session == null ? null : session.session();
    }

    public boolean isSession(@NotNull String sessionName) {
        return registeredSession.containsKey(sessionName);
    }

    public void startSession(@NotNull String sessionName) {
        lock.lock();
        try {
            final SessionData sessionData = registeredSession.get(sessionName);
            if (sessionData == null) {
                return;
            }
            closeSession(sessionName);
            final Session session = new Session(plugin, sessionData);
            final ScheduledTask task = plugin.getProxy().getScheduler().schedule(plugin, () -> closeSession(sessionName), sessionData.duration(), TimeUnit.MILLISECONDS);
            activeSessions.put(sessionName, new ActiveSession(session, task.getId()));
        } finally {
            lock.unlock();
        }

    }

    public void closeSession(@NotNull String sessionName) {
        lock.lock();
        try {
            final ActiveSession activeSession = activeSessions.remove(sessionName);
            if (activeSession == null) {
                return;
            }
            plugin.getProxy().getScheduler().cancel(activeSession.taskId());
        } finally {
            lock.unlock();
        }
    }

    private static record ActiveSession (@NotNull Session session, int taskId) {}

}
