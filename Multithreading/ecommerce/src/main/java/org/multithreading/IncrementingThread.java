package org.multithreading;

public class IncrementingThread extends Thread {
    private final InventoryCounter inventoryCounter;
    private final int increments;

    public IncrementingThread(InventoryCounter inventoryCounter, int increments) {
        this.inventoryCounter = inventoryCounter;
        this.increments = increments;
    }

    @Override
    public void run() {
        for (int i = 0; i < increments; i++) {
            inventoryCounter.increment();
        }
    }
}
