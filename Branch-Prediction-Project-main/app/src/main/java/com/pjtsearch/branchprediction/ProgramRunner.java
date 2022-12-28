package com.pjtsearch.branchprediction;

import java.util.Optional;

public interface ProgramRunner extends Iterable<ProgramRunner.Result> {
    public record Result(Cache<String> instructionCache, Cache<Integer> dataCache, RegisterFile<Integer> registerFile, Optional<ProgramCounter> pc) {
        public static Result from(PipelinedExecutionState state) {
            return new Result(state.caches().instructionCache(), state.caches().dataCache(), state.caches().registerFile(), state.caches().pc());
        }
        public static Result from(SequentialExecutionState.SumType state) {
            return new Result(state.caches().instructionCache(), state.caches().dataCache(), state.caches().registerFile(), state.caches().pc());
        }
    }
}
