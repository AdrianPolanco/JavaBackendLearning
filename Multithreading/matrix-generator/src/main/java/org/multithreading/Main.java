package org.multithreading;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final String INPUT_FILE = "./out/matrices";
    public static final String OUTPUT_FILE = "./out/matrices_results.txt";
    public static final int N = 10;

    public static void main(String[] args) throws IOException {
        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        MatricesReaderProducer matricesReader = new MatricesReaderProducer(new FileReader(inputFile), threadSafeQueue);
        MatricesMultiplierConsumer matricesConsumer = new MatricesMultiplierConsumer(new FileWriter(outputFile), threadSafeQueue);

        matricesConsumer.start();
        matricesReader.start();
    }
}