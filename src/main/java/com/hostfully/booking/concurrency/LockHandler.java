package com.hostfully.booking.concurrency;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class LockHandler {
    private final Set<String> keys = ConcurrentHashMap.newKeySet();

    public <T> T lockAndExecuteSupplier(String key, Long id, Supplier<T> supplier) {
        if (tryLock(key, id)) {
            try {
                return supplier.get();
            } finally {
                unlock(key, id);
            }
        } else {
            throw new IllegalStateException("Entity is locked");
        }
    }

    private boolean tryLock(String key, Long id) {
        return keys.add(getKey(key, id));
    }

    private void unlock(String key, Long id) {
        keys.remove(getKey(key, id));
    }

    private static String getKey(String key, Long id) {
        return String.format("%s-%s", key, id);
    }
}
