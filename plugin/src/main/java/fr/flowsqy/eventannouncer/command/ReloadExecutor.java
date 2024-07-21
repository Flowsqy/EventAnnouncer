package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class ReloadExecutor implements Executor {

    private final Plugin plugin;
    private final RootCommand rootCommand;
    private final BaseComponent successMessage;

    public ReloadExecutor(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig, @NotNull RootCommand rootCommand) {
        this.plugin = plugin;
        this.rootCommand = rootCommand;
        this.successMessage = messageConfig.getComponentMessage("command.reload");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        final RootCommandLoader rootCommandLoader = new RootCommandLoader();
        rootCommandLoader.load(plugin, rootCommand); 
        sender.sendMessage(successMessage);
    }

}
