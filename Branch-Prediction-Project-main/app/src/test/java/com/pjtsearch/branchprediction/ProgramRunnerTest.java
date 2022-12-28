package com.pjtsearch.branchprediction;

import com.google.common.collect.Streams;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProgramRunnerTest<T extends ProgramRunner> {
    protected ProgramResult runProgram(String fileName, Function<String, T> runner) throws IOException, URISyntaxException {
        URI uri = Resources.getResource("com.pjtsearch.branchprediction/" + fileName).toURI();
        return Streams.findLast(Streams.mapWithIndex(
                Streams.stream(runner.apply(Files.readString(Paths.get(uri)))),
                (result, i) -> new ProgramResult(result.registerFile().get(33), (int) i + 1)
        )).get();
    }

    protected record ProgramResult(int result, int cycles) {}
}
