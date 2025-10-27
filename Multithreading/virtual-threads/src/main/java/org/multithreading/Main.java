package org.multithreading;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static int NUMBER_OF_VIRTUAL_THREADS = 20;
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Runnable runnable = () -> System.out.println("Inside thread: " + Thread.currentThread());

        List<Thread> virtualThreads = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            virtualThreads.add(virtualThread);
        }

        for(Thread vt : virtualThreads) {
            vt.start();
        }

        for(Thread vt : virtualThreads) {
            vt.join();
        }
    }
}