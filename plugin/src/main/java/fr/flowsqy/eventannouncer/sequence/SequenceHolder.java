package fr.flowsqy.eventannouncer.sequence;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class SequenceHolder {

    private Map<String, InformationsData[]> sequences;

    @NotNull
    public Map<String, InformationsData[]> getSequences() {
        if (sequences == null) {
            throw new IllegalStateException("Sequences not initialized");
        }
        return sequences;
    }

    public void setSequences(@NotNull Map<String, InformationsData[]> sequences) {
        this.sequences = sequences;
    }

}
