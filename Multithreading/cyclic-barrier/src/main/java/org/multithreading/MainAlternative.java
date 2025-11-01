package org.multithreading;

import java.util.ArrayList;
import java.util.List;

public class MainAlternative {
    public static void main(String[] args) throws InterruptedException {

        // Simple sequential single-threaded processing
        int result = 0;
        List<Integer> results = new ArrayList<>();
        System.out.println("This is an alternative main class.");
        var startTime = System.currentTimeMillis();
        List<Integer> ns = List.of(1000, 2000);
        for(Integer n : ns){
            for(int i = 0; i < n; i++) {
                System.out.println("Processing step " + (i + 1) + " of " + n);
                result += 1;
                Thread.sleep(1); // Simulate time taken for each step
            }
        }

        var endTime = System.currentTimeMillis();

        System.out.println("Final result: " + result + ". Time taken: " + (endTime - startTime) + " ms.");
    }
}
