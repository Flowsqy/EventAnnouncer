package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import fr.flowsqy.eventannouncer.load.PluginData;
import fr.flowsqy.eventannouncer.load.PluginLoader;
import fr.flowsqy.eventannouncer.session.RequestTeleportListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class ReloadExecutor implements Executor {

    private final Plugin plugin;
    private final RootCommand rootCommand;
    private final RequestTeleportListener requestTeleportListener;
    private final BaseComponent failMessage;
    private final BaseComponent successMessage;

    public ReloadExecutor(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig,
            @NotNull RootCommand rootCommand, @NotNull RequestTeleportListener requestTeleportListener) {
        this.plugin = plugin;
        this.rootCommand = rootCommand;
        this.requestTeleportListener = requestTeleportListener;
        this.failMessage = messageConfig.getComponentMessage("command.reload.fail");
        this.successMessage = messageConfig.getComponentMessage("command.reload.success");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        final PluginLoader pluginLoader = new PluginLoader();
        final PluginData pluginData = pluginLoader.load(plugin);
        if (pluginData == null) {
            sender.sendMessage(failMessage);
            return;
        }
        requestTeleportListener.load(pluginData.sessionManager());
        final RootCommandLoader rootCommandLoader = new RootCommandLoader();
        rootCommandLoader.load(plugin, pluginData, rootCommand, requestTeleportListener);
        sender.sendMessage(successMessage);
    }

}
