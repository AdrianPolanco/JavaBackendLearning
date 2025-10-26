package org.multithreading;

public class InventoryCounter {
    private int items = 0;

    public synchronized void increment() {
        items++;
    }

    public synchronized void decrement() {
        if (items > 0) {
            items--;
        }
    }

    public synchronized int getItems() {
        return items;
    }
}
