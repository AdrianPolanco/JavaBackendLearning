package org.multithreading;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InventoryDatabase {
    private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
        readLock.lock();
        try {
            Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
            Integer toKey = priceToCountMap.floorKey(upperBound);

            if (fromKey == null || toKey == null) {
                return 0;
            }

            NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(
                    fromKey,
                    true,
                    toKey,
                    true);

            int sum = rangeOfPrices.values().stream().mapToInt(Integer::intValue).sum();

            return sum;
        } finally {
            readLock.unlock();
        }
    }

    public void addItem(int price){
        /*Integer numberOfItemsForPrice = priceToCountMap.get(price);
        if(numberOfItemsForPrice == null) {
            priceToCountMap.put(price, 1);
        } else {
            priceToCountMap.put(price, numberOfItemsForPrice + 1);
        }*/

        writeLock.lock();
        try {
            //Equivalent to the above commented code
            priceToCountMap.merge(price, 1, Integer::sum);
        }finally {
            writeLock.unlock();
        }
    }

    public void removeItem(int price){
        writeLock.lock();

        try{
            Integer numberOfItemsForPrice = priceToCountMap.get(price);
            if(numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                priceToCountMap.remove(price);
            } else {
                priceToCountMap.put(price, numberOfItemsForPrice - 1);
            }
        }finally {
            writeLock.unlock();
        }
    }
}
