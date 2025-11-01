package org.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int NUM_WORKER_THREADS = 2;
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_WORKER_THREADS,
            () -> System.out.println("All worker threads have reached the barrier and are synchronized."));

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        System.out.println("Main thread is preparing worker threads...");
        List<WorkerThread> workerThreads = new ArrayList<>();
        var number = new AtomicInteger(0);
        for(int i = 0; i < NUM_WORKER_THREADS; i++){
            int n = i + 1;
            WorkerThread workerThread = new WorkerThread(
                    "Worker-" + n,
                    n,
                    cyclicBarrier,
                    number
            );
            workerThreads.add(workerThread);
        }

        var startTime = System.currentTimeMillis();
        // Start all worker threads
        for(WorkerThread workerThread : workerThreads){
            workerThread.start();
        }
        // Wait for all worker threads to complete
        for(WorkerThread workerThread : workerThreads){
            workerThread.join();
        }

        //WARNING: Do not use await to wait for the worker threads since the threads
        // specified in the CyclicBarrier are just 2 worker threads, if there are more than 2 threads
        // The next ones will wait forever.
        // cyclicBarrier.await();
        var endTime = System.currentTimeMillis();
        System.out.println("Final result in " + (endTime - startTime) + " ms: " + number.get());
        System.out.println("All worker threads have finished their work. Main thread is exiting...");
    }
}