package org.multithreading;

import java.util.Random;

public class BusinessLogic extends Thread {
    private final Metrics metrics;
    private final Random random = new Random();

    public BusinessLogic(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void run() {
        while(true){
            long startTime = System.currentTimeMillis();

            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
            }

            long endTime = System.currentTimeMillis();
            metrics.addSample(endTime - startTime);
        }
    }
}
