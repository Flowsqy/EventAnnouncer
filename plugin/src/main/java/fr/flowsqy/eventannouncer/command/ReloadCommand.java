package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.config.MessageConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class ReloadCommand extends Command {

    private final static String NAME = "eventannouncerreload";
    private final static String PERMISSION = "eventannouncer.command.reload";
    private final static String[] ALIASES = new String[] { "earl" };

    private final Plugin plugin;
    private final BaseComponent successMessage;

    public ReloadCommand(@NotNull Plugin plugin, @NotNull MessageConfig messageConfig) {
        super(NAME, PERMISSION, ALIASES);
        this.plugin = plugin;
        setPermissionMessage(messageConfig.getMessage("command.dont-have-permission"));
        this.successMessage = messageConfig.getComponentMessage("command.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // TODO Reload the configuration
        sender.sendMessage(successMessage);
    }

}
