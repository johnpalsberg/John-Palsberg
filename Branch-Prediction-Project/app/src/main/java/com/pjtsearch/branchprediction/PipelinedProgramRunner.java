package com.pjtsearch.branchprediction;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PipelinedProgramRunner implements ProgramRunner {
    PipelinedExecutionState initialState;
    public PipelinedProgramRunner(String program) {
        ImmutableList<Integer> CacheBuilder = ImmutableList.copyOf(Collections.nCopies(34,0));
        RegisterFile<Integer> registerFile = new RegisterFile<Integer>().withLoad(CacheBuilder , 0);
        List<String> instructions = Arrays.asList(program.split("\n"));
        Cache<String> instructionCache = new Cache<String>().withLoad(instructions, 0);
        Cache<Integer> dataCache = new Cache<Integer>().withLoad(CacheBuilder, 0);
        PipelinedExecutionState.Caches caches = new PipelinedExecutionState.Caches(
                instructionCache,
                dataCache,
                registerFile,
                Optional.of(new ProgramCounter(0))
        );
        initialState = new PipelinedExecutionState(Optional.empty(), Optional.empty(), Optional.empty(), caches);
    }

    private static class RunnerIterator implements Iterator<ProgramRunner.Result> {
        PipelinedExecutionState currentState;
        public RunnerIterator(PipelinedExecutionState initialState) {
            currentState = initialState;
        }

        @Override
        public boolean hasNext() {
            return currentState.toWriteBack().isPresent() ||
                    currentState.toExecute().isPresent() ||
                    currentState.toDecode().isPresent() ||
                    currentState.caches().pc().isPresent();
        }

        @Override
        public ProgramRunner.Result next() {
            currentState = PipelinedExecution.tick(currentState);
            return ProgramRunner.Result.from(currentState);
        }
    }

    @NotNull
    @Override
    public Iterator<ProgramRunner.Result> iterator() {
        return new RunnerIterator(initialState);
    }
}
