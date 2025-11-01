package org.multithreading;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int NUMBER_OF_TASKS = 3000;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Press enter to start...");
        s.nextLine();
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long startTime = System.currentTimeMillis();
        performTasks();
        System.out.printf("All tasks completed in %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static void performTasks() {
        // Replaced by virtual thread executor: 1270 - 1610 ms as average for 3000 tasks
        // try (ExecutorService executorService = Executors.newCachedThreadPool()) {
        // 1132 ms - 1300 ms as average for 3000 tasks
         try(ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()){
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(Main::blockingIoOperation);
            }
        }


    }

    private static void blockingIoOperation(){
        System.out.println("Executing blocking I/O operation from thread: " + Thread.currentThread());

        try {
            Thread.sleep(1000); // Simulate blocking I/O operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}