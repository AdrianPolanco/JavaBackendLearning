package org.multithreading;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String text = new String(Files
                .readAllBytes(Paths.get(ThroughputHttpServer.INPUT_FILE)));
        ThroughputHttpServer.startHttpServer(text);
    }
}