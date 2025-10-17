package org.oop.exercises.pricing.entities;

import org.oop.exercises.pricing.enums.TaxRate;
import org.oop.exercises.pricing.utils.IdGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Item {
    private Integer id;
    private String name;
    private BigDecimal basePrice;
    private Integer quantity;


    public Item( String name, BigDecimal basePrice, Integer quantity) {
        this.id = IdGenerator.nextItemId();
        this.name = name;
        this.basePrice = basePrice;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name of the product cannot be null or empty");
        }

        this.name = name;
    }

    public BigDecimal getTaxAmountPerUnit(){
        if(basePrice == null) return BigDecimal.ZERO;

        return basePrice.multiply(TaxRate.STANDARD.getStandardRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalPrice() {
        // BigDecimal does not support arithmetic operators directly, you must use methods
        // return basePrice + (basePrice * 0.18);

        var unitPriceWithTax = this.getUnitPriceWithTax();

        return unitPriceWithTax.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getUnitPriceWithTax() {
        var taxAmount = this.getTaxAmountPerUnit();
        return basePrice.add(taxAmount);
    }

    public void setBasePrice(BigDecimal basePrice) {
        if(basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Base price of the product cannot be null or negative");
        }

        this.basePrice = basePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if(quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity of the product cannot be null or negative");
        }

        this.quantity = quantity;
    }

    public void increaseQuantity(Integer amount) {
        if(amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount to increase cannot be null or negative or zero");
        }

        this.quantity += amount;
    }

    public void decreaseQuantity(Integer amount) {
        if(amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount to decrease cannot be null or negative or zero");
        }
        if(this.quantity - amount < 0) {
            throw new IllegalArgumentException("Quantity of the product cannot be negative");
        }

        this.quantity -= amount;
    }
}
