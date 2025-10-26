package org.multithreading;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter, 1000);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter, 1000);

        incrementingThread.start();
        decrementingThread.start();

        try {
            incrementingThread.join();
            decrementingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final inventory count: " + inventoryCounter.getCount());
    }
}