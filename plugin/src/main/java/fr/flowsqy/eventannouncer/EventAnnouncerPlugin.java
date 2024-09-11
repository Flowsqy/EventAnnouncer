package fr.flowsqy.eventannouncer;

import fr.flowsqy.eventannouncer.command.CommandLoader;
import fr.flowsqy.eventannouncer.load.PluginData;
import fr.flowsqy.eventannouncer.load.PluginLoader;
import fr.flowsqy.eventannouncer.session.RequestTeleportListener;
import net.md_5.bungee.api.plugin.Plugin;

public class EventAnnouncerPlugin extends Plugin {

    @Override
    public void onEnable() {
        final PluginLoader pluginLoader = new PluginLoader();
        final PluginData pluginData = pluginLoader.load(this);
        if (pluginData == null) {
            return;
        }
        final RequestTeleportListener requestTeleportListener = new RequestTeleportListener();
        getProxy().getPluginManager().registerListener(this, requestTeleportListener);
        requestTeleportListener.load(pluginData.sessionManager());
        final CommandLoader commandLoader = new CommandLoader();
        commandLoader.load(this, pluginData, requestTeleportListener);
    }

}
