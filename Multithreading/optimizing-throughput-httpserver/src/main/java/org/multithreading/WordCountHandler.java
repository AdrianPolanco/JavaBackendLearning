package org.multithreading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class WordCountHandler implements HttpHandler {

    private final String text;

    WordCountHandler(String text) {
        this.text = text;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
