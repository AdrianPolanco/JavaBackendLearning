package org.multithreading;

public class BlockingTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Starting blocking task in thread: " + Thread.currentThread());
        try {
            // Simulate a blocking operation
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Completed blocking task in thread: " + Thread.currentThread());
    }
}
