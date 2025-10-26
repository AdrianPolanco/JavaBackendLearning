package org.multithreading;

public class IncremetingThread extends Thread {
    private final InventoryCounter inventoryCounter;

    public IncremetingThread(InventoryCounter inventoryCounter) {
        this.inventoryCounter = inventoryCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            inventoryCounter.increment();
        }
    }
}
