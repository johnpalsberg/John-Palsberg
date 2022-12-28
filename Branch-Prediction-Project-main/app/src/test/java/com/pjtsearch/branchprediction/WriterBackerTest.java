package com.pjtsearch.branchprediction;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.pjtsearch.branchprediction.*;
import org.junit.jupiter.api.*;

import com.google.common.collect.ImmutableList;

public class WriterBackerTest {
    static RegisterFile<Integer> registerFile;
    static Cache<Integer> dataCache;
    
    @BeforeAll
    public static void setup() { 
    ImmutableList<Integer> CacheBuilder = ImmutableList.copyOf(Collections.nCopies(34,0));
    registerFile = new RegisterFile<Integer>();
    dataCache = new Cache<Integer>();
    registerFile = registerFile.withLoad(CacheBuilder , 0);
    dataCache = dataCache.withLoad(CacheBuilder, 0);
    }
    
    @Test
    void shouldWriteBackRegister() {
        List<WriteIndicator.SumType> executeBuffer = List.of(new WriteIndicator.WriteRegister(1, 1));
        WriterBacker.Output out = WriterBacker.writeBack(executeBuffer, registerFile, dataCache, Optional.of(new ProgramCounter(0)));
        assertEquals(1, out.registerFile().get(1));
    }

    @Test
    void shouldWriteBackMemory() {
        List<WriteIndicator.SumType> executeBuffer = List.of(new WriteIndicator.WriteMemory(1, 1));
        WriterBacker.Output out = WriterBacker.writeBack(executeBuffer, registerFile, dataCache, Optional.of(new ProgramCounter(0)));
        assertEquals(1, out.dataCache().get(1));
    }
 
    @Test
    void shouldWriteBackMultiple() {
        List<WriteIndicator.SumType> executeBuffer = List.of(new WriteIndicator.WriteRegister(1, 1), new WriteIndicator.WriteMemory(2, 2));
        WriterBacker.Output out = WriterBacker.writeBack(executeBuffer, registerFile, dataCache, Optional.of(new ProgramCounter(0)));
        assertEquals(1, out.registerFile().get(1));
        assertEquals(2, out.dataCache().get(2));
    }

}
