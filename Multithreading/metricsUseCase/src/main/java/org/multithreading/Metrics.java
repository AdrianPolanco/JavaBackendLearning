package org.multithreading;

public class Metrics {
    private long count = 0;
    private volatile double average = 0.0; // making average volatile to ensure visibility across threads

    public synchronized void addSample(long sample){
        double currentSum = average * count;
        count++;
        average = (currentSum + sample) / count;
    }

    // Atomic by default since average is volatile, hence, atomic read
    public double getAverage() {
        return average;
    }
}
