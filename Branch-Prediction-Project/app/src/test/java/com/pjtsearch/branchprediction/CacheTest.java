package com.pjtsearch.branchprediction;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {
    @Test
    void shouldConstruct() {
        assertDoesNotThrow(() -> new Cache<String>());
    }

    @Test
    void shouldPut() {
        Cache<String> cache = new Cache<>();
        cache = cache.withPut(0, "test");
        assertEquals(1, cache.entries().size());
        assertEquals(Set.of(Map.entry(0, "test")), cache.entries());
        cache = cache.withPut(5, "test2");
        assertEquals(2, cache.entries().size());
        assertEquals(Set.of(Map.entry(0, "test"), Map.entry(5, "test2")), cache.entries());
    }

    @Test
    void shouldGet() {
        Cache<String> cache = new Cache<>();
        cache = cache.withPut(0, "test");
        assertEquals("test", cache.get(0));
        cache = cache.withPut(5, "test2");
        assertEquals("test", cache.get(0));
        assertEquals("test2", cache.get(5));
    }

    @Test
    void shouldLoad() {
        Cache<String> cache = new Cache<>();
        cache = cache.withLoad(List.of("test", "test2", "test10", "test5"), 0);
        assertEquals(4, cache.entries().size());
        assertEquals(Set.of(Map.entry(0, "test"), Map.entry(1, "test2"), Map.entry(2, "test10"), Map.entry(3, "test5")), cache.entries());
        cache = cache.withLoad(List.of("test3", "test4"), 2);
        assertEquals(Set.of(Map.entry(0, "test"), Map.entry(1, "test2"), Map.entry(2, "test3"), Map.entry(3, "test4")), cache.entries());
        cache = cache.withLoad(List.of("test5", "test6"), 4);
        assertEquals(Set.of(Map.entry(0, "test"), Map.entry(1, "test2"), Map.entry(2, "test3"), Map.entry(3, "test4"), Map.entry(4, "test5"), Map.entry(5, "test6")), cache.entries());
    }

    @Test
    void shouldConvertToString() {
        Cache<String> cache = new Cache<>();
        cache = cache.withLoad(List.of("test", "test2", "test10", "test5"), 0);
        assertEquals("[0=test, 1=test2, 2=test10, 3=test5]", cache.toString());
    }
}
