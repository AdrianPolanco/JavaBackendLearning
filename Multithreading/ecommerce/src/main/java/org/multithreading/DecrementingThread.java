package org.multithreading;

public class DecrementingThread extends Thread {
    private final InventoryCounter inventoryCounter;
    private final int decrements;

    public DecrementingThread(InventoryCounter inventoryCounter, int decrements) {
        this.inventoryCounter = inventoryCounter;
        this.decrements = decrements;
    }

    @Override
    public void run() {
        for (int i = 0; i < decrements; i++) {
            inventoryCounter.decrement();
        }
    }
}
