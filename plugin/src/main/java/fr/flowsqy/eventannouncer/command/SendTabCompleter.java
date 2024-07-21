package fr.flowsqy.eventannouncer.command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.eventannouncer.sequence.InformationsData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

public class SendTabCompleter implements TabExecutor {

    private final Map<String, InformationsData[]> sequences;

    public SendTabCompleter(@NotNull Map<String, InformationsData[]> sequences) {
        this.sequences = sequences;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        final List<String> completions = new LinkedList<>();
        for (String sequenceName : sequences.keySet()) {
            if (sequenceName.startsWith(args[1])) {
                completions.add(sequenceName);
            }
        }
        return completions;
    }

}
