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

public class PipelinedProgramRunnerTest extends ProgramRunnerTest<PipelinedProgramRunner> {
    private static final Map<String, ProgramResult> programs = ImmutableMap.of(
            "Program1.csv", new ProgramResult(215, 8),
            "Program2.csv", new ProgramResult(10, 43),
            "Multiply.csv", new ProgramResult(24, 46),
            "Branch.csv", new ProgramResult(16, 13),
            "DataCacheOp.csv", new ProgramResult(10, 9)
    );
    @Test
    void programsShouldReturnCorrectly() {
        programs.forEach((file, expected) -> {
            try {
                assertEquals(
                        expected,
                        runProgram(file, PipelinedProgramRunner::new)
                );
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
