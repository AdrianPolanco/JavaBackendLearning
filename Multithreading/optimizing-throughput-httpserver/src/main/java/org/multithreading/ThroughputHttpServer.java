package org.multithreading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    public static String INPUT_FILE = "src/main/resources/war_and_peace.txt";
    public static int NUMBER_OF_THREADS = 1;
    public static void startHttpServer(String text) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

        httpServer.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executor);
        httpServer.start();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
