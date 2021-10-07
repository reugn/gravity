package io.github.reugn.gravity.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxEntries;

    public LRUCache(int maxEntries, int initialCapacity) {
        super(initialCapacity);
        this.maxEntries = maxEntries;
    }

    public LRUCache(int maxEntries) {
        super();
        this.maxEntries = maxEntries;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }
}
