package org.multithreading.exercises.factorial;

import java.math.BigInteger;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Long> numbers = List.of(
                0L, 587L, 234L, 7654L, 8765L, 2345L, 4567L, 6789L, 7890L, 8901L, 3456L, 8901L, 9012L,
                3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L, 7897L, 8909L, 9010L, 8901L, 5678L, 7890L,
                2345L, 6577L, 8765L, 2345L, 1234L, 9876L, 3456L, 4567L, 5678L, 6789L, 7890L, 8901L, 9012L);

        // Sequential calculation
        // Create cronometer start time
        long startTime = System.currentTimeMillis();

        /*for(Long number: numbers){
            FactorialThread.factorial(number);
            System.out.println("Factorial of " + number + " calculated.");
        }*/

        List<FactorialThread> threads = numbers.stream()
                .map(FactorialThread::new)
                .toList();

        // Start all threads
        for(FactorialThread thread : threads){
            thread.start();
        }

        for(FactorialThread thread : threads){
            try {
                // Wait for each thread to finish
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Wait for all threads to finish
        for(int i = 0; i < threads.size(); i++){
            FactorialThread thread = threads.get(i);
            if(thread.isFinished()){
                System.out.println("Factorial of " + numbers.get(i) + " is " + thread.getResult());
            }else{
                System.out.println("Factorial of " + numbers.get(i) + " is still being calculated.");
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Total time taken (sequential): " + (endTime - startTime) + " ms");
    }

    public static class FactorialThread extends Thread {
        private final long inputNumber;
        private BigInteger result = BigInteger.ONE;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            result = factorial(inputNumber);
            isFinished = true;
        }

        public static BigInteger factorial(long n){
            BigInteger tempResult = BigInteger.ONE;

            for(long i = n; i > 0; i--){
                tempResult = tempResult.multiply(BigInteger.valueOf(i));
            }

            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
