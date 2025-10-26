package org.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryCounter {
    private final AtomicInteger count  = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public void decrement() {
        count.decrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
