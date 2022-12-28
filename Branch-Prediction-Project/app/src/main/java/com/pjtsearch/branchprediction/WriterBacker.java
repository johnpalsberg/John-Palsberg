package com.pjtsearch.branchprediction;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WriterBacker {
    public record Output(RegisterFile<Integer> registerFile, Cache<Integer> dataCache, Optional<ProgramCounter> pc) {}

    public static WriterBacker.Output writeBack(List<WriteIndicator.SumType> executeBuffer, RegisterFile<Integer> registerFile, Cache<Integer> dataCache, Optional<ProgramCounter> pc) {
        RegisterFile<Integer> newRegisterFile = new RegisterFile<>(registerFile);
        Cache<Integer> newDataCache = new Cache<>(dataCache);
        Optional<ProgramCounter> newPC = pc.map((currentPc) -> new ProgramCounter(currentPc.value()));
        for (WriteIndicator.SumType operation : executeBuffer) {
            if (operation instanceof WriteIndicator.WriteRegister op) {
               newRegisterFile = newRegisterFile.withPut(op.address(), op.data());
            } else if (operation instanceof WriteIndicator.WriteMemory op) {
                newDataCache = newDataCache.withPut(op.address(), op.data());
            } else if (operation instanceof WriteIndicator.WritePC op) {
                newPC = Optional.of(new ProgramCounter(op.pcVal()));
            }
        }
        return new WriterBacker.Output(newRegisterFile, newDataCache, newPC);
    }

}
