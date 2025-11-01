package org.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final CountDownLatch startCountDownLatch = new CountDownLatch(1);
    private static final int NUM_WORKER_THREADS = 2;
    private static final CountDownLatch doneCountDownLatch = new CountDownLatch(NUM_WORKER_THREADS);

    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Main thread is preparing worker threads...");
        List<WorkerThread> workerThreads = new ArrayList<>();
        var number = new AtomicInteger(0);
        for(int i = 0; i < NUM_WORKER_THREADS; i++){
            int n = i + 1;
            WorkerThread workerThread = new WorkerThread(
                    "Worker-" + n,
                    n,
                    startCountDownLatch,
                    doneCountDownLatch,
                    number
            );
            workerThreads.add(workerThread);
        }

        // Start all worker threads
        for(WorkerThread workerThread : workerThreads){
            workerThread.start();
        }

        System.out.println("Main thread is about to signal worker threads to start...");
        Thread.sleep(500); // Simulate some preparation time
        // Signal all worker threads to start working
        startCountDownLatch.countDown();
        var startTime = System.currentTimeMillis();

        // Wait for all worker threads to complete
        try {
            doneCountDownLatch.await();
            var endTime = System.currentTimeMillis();
            System.out.println("Final result in " + (endTime - startTime) + " ms: " + number.get());
            System.out.println("All worker threads have finished their work. Main thread is exiting...");
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted while waiting for worker threads to finish.");
        }
    }
}