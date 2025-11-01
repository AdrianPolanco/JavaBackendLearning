package org.multithreading;

import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerThread extends Thread {
    private final Duration workDuration;
    private final CyclicBarrier cyclicBarrier;
    private final int millis;
    private AtomicInteger n;

    public WorkerThread(String name, int durationNumberInSeconds, CyclicBarrier cyclicBarrier, AtomicInteger n) {
        super(name);
        this.millis = durationNumberInSeconds * 1000;
        this.workDuration = Duration.ofMillis(millis); // Work duration based on n
        this.cyclicBarrier = cyclicBarrier;
        this.n = n;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is waiting to start...");
        try {
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
            System.out.println(getName() + " is waiting at the barrier.");
            cyclicBarrier.await();
            System.out.println(getName() + " total time taken: " + (endTime - startTime) + " ms.");
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted.");
        } catch(BrokenBarrierException e){
            System.out.println(getName() + " encountered a broken barrier.");
        }
    }
}
