package org.oop.exercises.pricing.utils;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final AtomicInteger invoiceIdCounter = new AtomicInteger(0);
    private static final AtomicInteger itemIdCounter = new AtomicInteger(0);
    private static final AtomicInteger orderIdCounter = new AtomicInteger(0);

    private IdGenerator() {
        // Evita instanciar la clase
    }

    public static int nextInvoiceId() {
        return invoiceIdCounter.incrementAndGet();
    }

    public static int nextItemId() {
        return itemIdCounter.incrementAndGet();
    }

    public static int nextOrderId() { return orderIdCounter.incrementAndGet(); }
}

