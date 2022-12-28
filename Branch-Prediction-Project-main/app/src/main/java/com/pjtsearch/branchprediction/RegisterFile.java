package com.pjtsearch.branchprediction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

public class RegisterFile<T> {
    private ImmutableMap<Integer, T> storage = ImmutableMap.of();

    private RegisterFile(ImmutableMap<Integer, T> storage) {
        this.storage = storage;
    }

    public RegisterFile() {
    }

    public RegisterFile(RegisterFile<T> cache) {
        this.storage = ImmutableMap.copyOf(cache.storage);
    }
    public RegisterFile<T> withPut(int address, T data) {
        Map<Integer, T> copy = new LinkedHashMap<>(storage);
        copy.put(address, data);
        return new RegisterFile<T>(ImmutableMap.copyOf(copy));
    }

    public T get(int address) {
        return storage.get(address);
    }

    public RegisterFile<T> withLoad(List<T> data, int startAddress) {
        RegisterFile<T> acc = this;
        for (int i = 0; i < data.size(); i++) {
            acc = acc.withPut(startAddress + i, data.get(i));
        }
        return acc;
    }

    public Set<Map.Entry<Integer, T>> entries() {
        return storage.entrySet();
    }

    @Override
    public String toString() {
        return entries().toString();
    }
}
