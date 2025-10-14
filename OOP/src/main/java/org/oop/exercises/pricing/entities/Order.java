package org.oop.exercises.pricing.entities;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.utils.IdGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Integer id;
    private BigDecimal totalPrice;
    private List<Invoice> invoicesList;
    private LocalDateTime createdAt;

    private Order(Integer id, BigDecimal totalPrice, List<Invoice> invoicesList) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.invoicesList = invoicesList;
        this.createdAt = LocalDateTime.now();
    }

    public static Order of(List<PricingDto> pricingDtos) {
        if(pricingDtos == null || pricingDtos.isEmpty())
            throw new IllegalArgumentException("The order must contain at least one item");

        var invoices = pricingDtos.stream().map(Invoice::of).toList();

        var totalPrice = calculateTotalPrice(invoices);

        return new Order(IdGenerator.nextOrderId(), totalPrice, invoices);
    }

    private static BigDecimal calculateTotalPrice(List<Invoice> invoices){
        return invoices.stream()
                .map(Invoice::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
