package org.multithreading;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncremetingThread incremetingThread = new IncremetingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incremetingThread.start();
        decrementingThread.start();

        incremetingThread.join(); // Wait for incrementing thread to finish before starting decrementing thread
        decrementingThread.join(); // Wait for decrementing thread to finish

        System.out.println("Final item count: " + inventoryCounter.getItems());
    }
}