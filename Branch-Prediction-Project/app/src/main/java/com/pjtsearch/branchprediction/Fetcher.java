package com.pjtsearch.branchprediction;

import java.util.Optional;

public class Fetcher {
    public record Output(String rawInstruction, Optional<ProgramCounter> pc) {
    }
  public static Output fetch(Cache<String> cache, ProgramCounter pc) {

      return new Output(
              cache.get(pc.value()),
              cache.get(pc.value() + 1) != null ? Optional.of(new ProgramCounter(pc.value()+1)) : Optional.empty()
      );

  }
}
