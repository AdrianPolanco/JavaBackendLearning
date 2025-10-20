package org.multithreading;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("We are in thread: %s before creating new thread %n", Thread.currentThread().getName());
        /*var thread = new Thread(() -> {
            System.out.printf("We are in thread: %s inside new thread %n", Thread.currentThread().getName());
            System.out.printf("Thread priority is: %d %n", Thread.currentThread().getPriority());
            throw new RuntimeException("Intentional Exception");
        });*/

        //ALTERNTIVE
        var thread = new CustomThread();

        // Set a custom name for the thread
        thread.setName("secondary-thread");

        // Modifying dynamic priority through setting the static priority
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler((secondaryThread, throwable) -> {
            System.out.printf("Thread %s threw exception: %s %n", secondaryThread.getName(), throwable.getMessage());
        });

        thread.start();
        System.out.printf("We are in thread: %s after starting new thread %n", Thread.currentThread().getName());

        thread.join(); // Wait for the new thread to finish
    }

    private static class CustomThread extends Thread {
        @Override
        public void run() {
            System.out.printf("We are in thread: %s inside new thread %n", this.getName());
            System.out.printf("Thread priority is: %d %n", this.getPriority());
            throw new RuntimeException("Intentional Exception");
        }
    }
}