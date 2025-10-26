package org.multithreading;

public class MetricsPrinter extends Thread {
    private final Metrics metrics;

    public MetricsPrinter(Metrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            double currentAverage = metrics.getAverage();
            System.out.printf("Current average response time: %.2f ms%n", currentAverage);
        }
    }
}
