package com.pjtsearch.branchprediction;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class SequentialProgramRunner implements ProgramRunner {
    SequentialExecutionState.SumType initialState;
    public SequentialProgramRunner(String program) {
        ImmutableList<Integer> CacheBuilder = ImmutableList.copyOf(Collections.nCopies(34,0));
        RegisterFile<Integer> registerFile = new RegisterFile<Integer>().withLoad(CacheBuilder , 0);
        List<String> instructions = Arrays.asList(program.split("\n"));
        Cache<String> instructionCache = new Cache<String>().withLoad(instructions, 0);
        Cache<Integer> dataCache = new Cache<Integer>().withLoad(CacheBuilder, 0);
        initialState = new SequentialExecutionState.ToFetch(new SequentialExecutionState.Caches(
                instructionCache,
                dataCache,
                registerFile,
                Optional.of(new ProgramCounter(0))
        ));
    }

    private static class RunnerIterator implements Iterator<ProgramRunner.Result> {
        SequentialExecutionState.SumType currentState;
        public RunnerIterator(SequentialExecutionState.SumType initialState) {
            currentState = initialState;
        }

        @Override
        public boolean hasNext() {
            return !(currentState instanceof SequentialExecutionState.Empty);
        }

        @Override
        public ProgramRunner.Result next() {
            try {
                currentState = SequentialExecution.tick(currentState);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return ProgramRunner.Result.from(currentState);
        }
    }

    @NotNull
    @Override
    public Iterator<ProgramRunner.Result> iterator() {
        return new RunnerIterator(initialState);
    }
}
