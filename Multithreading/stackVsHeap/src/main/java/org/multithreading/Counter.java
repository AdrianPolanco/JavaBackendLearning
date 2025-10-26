package org.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    /*AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.getAndIncrement();
    }*/

    int count = 0;

    public synchronized void increment() {
        count++;
    }
}
