package com.pjtsearch.branchprediction;

import java.io.IOException;
import java.util.List;
import java.util.Optional;



public class PipelinedExecution {

    public static PipelinedExecutionState tick(PipelinedExecutionState state) {
        PipelinedExecutionState.Caches writeBackCaches = state.toWriteBack().map((toWriteBack) -> {
            WriterBacker.Output writtenBack = WriterBacker.writeBack(toWriteBack.executeBuffer(), state.caches().registerFile(), state.caches().dataCache(), state.caches().pc());
            return new PipelinedExecutionState.Caches(state.caches().instructionCache(), writtenBack.dataCache(), writtenBack.registerFile(), writtenBack.pc());
        }).orElse(state.caches());
        Optional<PipelinedExecutionState.ToWriteBack> nextToWriteBack = state.toExecute().map((toExecute) -> {
            List<WriteIndicator.SumType> executed = Executor.execute(toExecute.instruction(), writeBackCaches.registerFile(), writeBackCaches.dataCache(), toExecute.instructionAddress());
            return new PipelinedExecutionState.ToWriteBack(executed);
        });

        Optional<Instruction.SumType> decoded = state.toDecode().map((toDecode) -> {
            try {
                return Decoder.decode(toDecode.instruction());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        PipelinedExecutionState.Caches decodeCaches = decoded
                .filter(dec -> List.of("Jal", "Jalr", "Beq", "Bne", "Blt", "Bge").contains(dec.getName()))
                .map(dec -> new PipelinedExecutionState.Caches(
                        state.caches().instructionCache(),
                        writeBackCaches.dataCache(),
                        writeBackCaches.registerFile(),
//                        If a new a new pc was written by writeback, this shouldn't run and overwrite it because
//                        there would be no instructions in the pipeline before it because it would have been locked.
//                        In the speculative execution, it is not important because the PC will never be locked and
//                        there will be no gaps in the pipeline; the next fetch will always be decided by the previous
                        Optional.empty()
                )).orElse(writeBackCaches);
        Optional<PipelinedExecutionState.ToExecute> nextToExecute = decoded.map((dec) ->
            new PipelinedExecutionState.ToExecute(dec, /* TODO: Refactor; always present */ state.toDecode().get().instructionAddress())
        );

        Optional<Fetcher.Output> fetched = decodeCaches.pc()
                .map((pc) ->
                    Fetcher.fetch(state.caches().instructionCache(), pc)
                 );
        Optional<PipelinedExecutionState.ToDecode> nextToDecode = fetched.map((fetch) ->
                new PipelinedExecutionState.ToDecode(fetch.rawInstruction(), /* TODO: Refactor; always present */ decodeCaches.pc().get().value())
        );
        PipelinedExecutionState.Caches fetchCaches = fetched.map((fetch) ->
                new PipelinedExecutionState.Caches(
                        state.caches().instructionCache(),
                        decodeCaches.dataCache(),
                        decodeCaches.registerFile(),
                        fetch.pc()
                )
        ).orElse(decodeCaches);

        return new PipelinedExecutionState(nextToDecode, nextToExecute, nextToWriteBack, fetchCaches);
    }
}
