package org.multithreading.exercises.minmax;

import java.util.ArrayList;
import java.util.List;

public class MinMaxMetrics {

    // Add all necessary member variables
    volatile long min;
    volatile long max;
    List<Long> samples = new ArrayList<>();

    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        min = 0;
        max = 0;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        samples.add(newSample);
        if (newSample < min || min == 0) {
            min = newSample;
        }
        if (newSample > max) {
            max = newSample;
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        // Add code here
        return min;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        return max;
    }
}
