package com.pjtsearch.branchprediction;

import java.util.List;
import java.util.Optional;

public final class SequentialExecutionState {
    public static sealed interface SumType permits ToFetch, ToDecode, ToExecute, ToWriteBack, Empty {
        Caches caches();

    }

    public static final record Caches(Cache<String> instructionCache, Cache<Integer> dataCache, RegisterFile<Integer> registerFile, Optional<ProgramCounter> pc) {}

    public static final record ToFetch(Caches caches) implements SumType {}
    public static final record ToDecode(String instruction, Caches caches, int instructionAddress) implements SumType {}
    public static final record ToExecute(Instruction.SumType instruction, Caches caches, int instructionAddress) implements SumType {}
    public static final record ToWriteBack(List<WriteIndicator.SumType> executeBuffer, Caches caches) implements SumType {}
    public static final record Empty(Caches caches) implements SumType {}
}