package com.pjtsearch.branchprediction;

import java.util.List;
import java.util.Optional;


public record PipelinedExecutionState(Optional<ToDecode> toDecode, Optional<ToExecute> toExecute, Optional<ToWriteBack> toWriteBack, Caches caches) {
    public static final record Caches(Cache<String> instructionCache, Cache<Integer> dataCache, RegisterFile<Integer> registerFile, Optional<ProgramCounter> pc) {}

    public static final record ToDecode(String instruction, int instructionAddress) {}
    public static final record ToExecute(Instruction.SumType instruction, int instructionAddress) {}
    public static final record ToWriteBack(List<WriteIndicator.SumType> executeBuffer) {}
}