package com.pjtsearch.branchprediction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A data structure for storing items with numeric addresses
 * @param <T> The type of the content
 */
public class Cache<T> {
    private ImmutableMap<Integer, T> storage = ImmutableMap.of();

    private Cache(ImmutableMap<Integer, T> storage) {
        this.storage = storage;
    }

    /**
     * Constructs a new Cache instance
     */
    public Cache() {
    }

    /**
     * Constructs a new Cache instance copying from another cache
     * @param cache The cache to copy from
     */
    public Cache(Cache<T> cache) {
        this.storage = ImmutableMap.copyOf(cache.storage);
    }

    /**
     * Creates a copy of this cache with data put at an address
     * @param address The address to put at
     * @param data The data to put
     * @return A copy of this cache with data put
     */
    public Cache<T> withPut(int address, T data) {
        Map<Integer, T> copy = new LinkedHashMap<>(storage);
        copy.put(address, data);
        return new Cache<T>(ImmutableMap.copyOf(copy));
    }

    /**
     * Gets the data at an address
     * @param address The address to fetch
     * @return The data at the address
     */
    public T get(int address) {
        return storage.get(address);
    }

    /**
     * Creates a copy of this cache with data put in order starting at an address
     * @param data The data set to put
     * @param startAddress The address to start putting
     * @return A copy of this cache with data put
     */
    public Cache<T> withLoad(List<T> data, int startAddress) {
        Cache<T> acc = this;
        for (int i = 0; i < data.size(); i++) {
            acc = acc.withPut(startAddress + i, data.get(i));
        }
        return acc;
    }

    /**
     * Gets the entries of this cache
     * @return The entries of this cache
     */
    public Set<Map.Entry<Integer, T>> entries() {
        return storage.entrySet();
    }

    @Override
    public String toString() {
        return entries().toString();
    }
}
