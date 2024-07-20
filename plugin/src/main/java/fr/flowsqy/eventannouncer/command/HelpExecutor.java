package fr.flowsqy.eventannouncer.command;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class HelpExecutor implements Executor {

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        sender.sendMessage(new TextComponent("Help"));
	}

}
