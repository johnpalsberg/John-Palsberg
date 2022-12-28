package com.pjtsearch.branchprediction;

import com.pjtsearch.branchprediction.SequentialExecutionState.Caches;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SequentialExecution {
    public static SequentialExecutionState.SumType tick(SequentialExecutionState.SumType state) throws IOException {
        if (state instanceof SequentialExecutionState.ToFetch castedState) {
            Optional<Fetcher.Output> fetched = castedState.caches().pc().map((pc) ->
                    Fetcher.fetch(castedState.caches().instructionCache(), pc)
            );
            return fetched.map((fetch) ->
                    (SequentialExecutionState.SumType) new SequentialExecutionState.ToDecode(
                            fetch.rawInstruction(),
                            new Caches(
                                    castedState.caches().instructionCache(),
                                    castedState.caches().dataCache(),
                                    castedState.caches().registerFile(),
                                    fetch.pc()
                            ),
                            /* TODO: Refactor; always present */castedState.caches().pc().get().value()
                    )
            ).orElse(new SequentialExecutionState.Empty(castedState.caches()));
        } else if (state instanceof SequentialExecutionState.ToDecode castedState) {
            Instruction.SumType decoded = Decoder.decode(castedState.instruction());
            return new SequentialExecutionState.ToExecute(decoded, castedState.caches(), castedState.instructionAddress());
        } else if (state instanceof SequentialExecutionState.ToExecute castedState) {
          List<WriteIndicator.SumType> executed = Executor.execute(castedState.instruction(),castedState.caches().registerFile(), castedState.caches().dataCache(), castedState.instructionAddress());
            return new SequentialExecutionState.ToWriteBack(executed, castedState.caches());
        } else if (state instanceof SequentialExecutionState.ToWriteBack castedState) {
            WriterBacker.Output writtenBack = WriterBacker.writeBack(castedState.executeBuffer(),castedState.caches().registerFile(), castedState.caches().dataCache(), castedState.caches().pc());
            Caches updatedCaches = new Caches(castedState.caches().instructionCache(), writtenBack.dataCache(), writtenBack.registerFile(), writtenBack.pc());
            return new SequentialExecutionState.ToFetch(updatedCaches);
        } else if (state instanceof SequentialExecutionState.Empty castedState) {
            return new SequentialExecutionState.Empty(castedState.caches());
        } else {
            throw new IllegalArgumentException();
        }
    }

}
