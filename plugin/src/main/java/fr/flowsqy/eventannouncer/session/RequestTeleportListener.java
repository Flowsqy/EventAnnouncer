package fr.flowsqy.eventannouncer.session;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class RequestTeleportListener implements Listener {

    private final static String COMMAND_NAME = "ea";
    private final static String SUB_COMMAND_NAME = "tp";

    private SessionManager sessionManager;

    public void load(@NotNull SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!event.isCommand()) {
            return;
        }
        final String[] args = event.getMessage().substring(1).split(" ");
        if (args.length != 3) {
            return;
        }
        if (!COMMAND_NAME.equals(args[0])) {
            return;
        }
        if (!SUB_COMMAND_NAME.equals(args[1])) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }
        final String sessionName = args[2];
        final SessionData sessionData = sessionManager.getData(sessionName);
        if (sessionData == null) {
            return;
        }
        event.setCancelled(true);
        final Session session = sessionManager.getSession(sessionName);
        if (session == null) {
            player.sendMessage(sessionData.notOpenedMessage());
            return;
        }
        session.requestTeleport(player);
    }

}
