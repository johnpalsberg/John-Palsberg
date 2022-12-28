package com.pjtsearch.branchprediction;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SequentialProgramRunnerTest extends ProgramRunnerTest<SequentialProgramRunner> {
    private static final Map<String, ProgramResult> programs = ImmutableMap.of(
            "Program1.csv", new ProgramResult(215, 21),
            "Program2.csv", new ProgramResult(10, 89),
            "Multiply.csv", new ProgramResult(24, 109),
            "Branch.csv", new ProgramResult(16, 25),
            "DataCacheOp.csv", new ProgramResult(10, 25)
    );
    @Test
    void programsShouldReturnCorrectly() {
        programs.forEach((file, expected) -> {
            try {
                assertEquals(
                        expected,
                        runProgram(file, SequentialProgramRunner::new)
                );
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
