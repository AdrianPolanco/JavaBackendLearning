package org.multithreading;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerThread extends Thread {
    private final Duration workDuration;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final int millis;
    private AtomicInteger n;

    public WorkerThread(String name, int durationNumberInSeconds, CountDownLatch startLatch, CountDownLatch doneLatch, AtomicInteger n) {
        super(name);
        this.millis = durationNumberInSeconds * 1000;
        this.workDuration = Duration.ofMillis(millis); // Work duration based on n
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.n = n;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is waiting to start...");
        try {
            // Wait for the signal to start
            startLatch.await();
            var startTime = System.currentTimeMillis();
            System.out.println(getName() + " started working.");
            System.out.println(getName() + " finished working after " + workDuration.toMillis() + " milliseconds.");
            // Signal that this thread has completed its work
            for(int i = 0; i < millis; i++) {
                System.out.println(getName() + " is processing step " + (i + 1) + " of with number " + n.get());
                n.incrementAndGet();
                System.out.println("Partial result in " + getName() + ": " + n.get());
                Thread.sleep(1); // Simulate time taken for each step
            }
            var endTime = System.currentTimeMillis();
            System.out.println(getName() + " total time taken: " + (endTime - startTime) + " ms.");
            doneLatch.countDown();
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted.");
        }
    }
}
