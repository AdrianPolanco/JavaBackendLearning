package org.multithreading.coordination;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
       /* Thread blockingThread = new Thread(new BlockingTask());
        blockingThread.start();

        blockingThread.interrupt();*/

        Thread longRunningThread = new Thread(new LongRunningTask(BigInteger.valueOf(2), BigInteger.valueOf(100000)));

        longRunningThread.start();
        // Set the thread as a daemon thread, so it will not prevent the JVM from exiting the main thread
        // Because the daemon thread will continue running in the background even after the main thread has finished
        // This has good use cases for background tasks like monitoring, processing, logging, etc.
        // longRunningThread.setDaemon(true);
        longRunningThread.interrupt();
    }

    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e) {
                System.out.println("Blocking task was interrupted");
            }
        }
    }

    private static class LongRunningTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongRunningTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run(){
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("Long running task was interrupted");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
