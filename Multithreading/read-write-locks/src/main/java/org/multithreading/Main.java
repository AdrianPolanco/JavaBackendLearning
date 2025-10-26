package org.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int HIGHEST_PRICE = 1000;
    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase database = new InventoryDatabase();
        Random random = new Random();

        for(int i = 0; i < 100000; i++) {
            database.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {
            while(true){
                database.addItem(random.nextInt(HIGHEST_PRICE));
                database.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
        });

        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;

        List<Thread> readers = new ArrayList<>();

        for(int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for(int i = 0; i < 100000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    database.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });

            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();

        for (Thread thread : readers) {
            thread.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.println("Reading took " + (endReadingTime - startReadingTime) + " ms");
    }
}