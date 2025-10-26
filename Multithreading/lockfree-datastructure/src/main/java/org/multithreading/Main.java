package org.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        StandardStack<Integer> standardStack = new StandardStack<>();
        Random random = new Random();

        for(int i = 0; i < 100000; i++){
            standardStack.push(random.nextInt());
        }

        List<Thread> threads = new ArrayList<>();

        int pushingThreads = 2;
        int poppingThreads = 2;

        for(int i = 0; i < pushingThreads; i++){
            Thread thread = new Thread(() -> {
                while(true){
                    standardStack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for(int i = 0; i < poppingThreads; i++){
            Thread thread = new Thread(() -> {
                while(true){
                    standardStack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for(Thread thread : threads){
            thread.start();
        }

        Thread.sleep(10000);

        System.out.println("%,d operations were performed in 10 seconds: " + standardStack.getCount());
    }
}