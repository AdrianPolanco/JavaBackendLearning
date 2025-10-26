package org.oop.exercises.pricing.entities;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.interfaces.PricingStrategy;
import org.oop.exercises.pricing.strategies.BundlePricingStrategy;
import org.oop.exercises.pricing.strategies.DiscountPricingStrategy;
import org.oop.exercises.pricing.strategies.StandardPricingStrategy;
import org.oop.exercises.pricing.utils.DiscountContext;
import org.oop.exercises.pricing.utils.IdGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public final class Invoice {
    private final Integer id;
    private final Item item;
    private final Integer billedQuantity;
    private final BigDecimal totalPrice;
    private final BigDecimal taxAmount;
    private final LocalDateTime createdAt;
    private final Set<DiscountContext> appliedDiscounts;
    private static final Map<DiscountType, PricingStrategy> pricingStrategies = Map.of(
            DiscountType.NONE, new StandardPricingStrategy(),
            DiscountType.DISCOUNT, new DiscountPricingStrategy(),
            DiscountType.BUNDLE, new BundlePricingStrategy()
    );

    private Invoice(Integer id, Item item, Integer billedQuantity, BigDecimal totalPrice, BigDecimal taxAmount, Set<DiscountContext> appliedDiscounts) {
        this.id = id;
        this.item = item;
        // EnumSet.of() usa menos memoria, es mas rapido y esta mas optimizado para enums que HashSet
        // this.discountTypes = EnumSet.of(DiscountType.NONE);
        this.billedQuantity = billedQuantity;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
        this.appliedDiscounts = appliedDiscounts;
        this.taxAmount = taxAmount;
    }

    public Integer getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public Set<DiscountContext> getAppliedDiscounts(){
        return appliedDiscounts;
    }

    public Integer getBilledQuantity() {
        return billedQuantity;
    }

    public Integer getQuantity(){
        return item.getQuantity();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getTotalTaxAmount() {
        return this.taxAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static Invoice of(PricingDto pricingDto){
        var item = pricingDto.item();
        var quantity = pricingDto.item().getQuantity();
        var billableQuantity = pricingDto.billedQuantity();
        var pendingDiscounts = pricingDto.pendingDiscounts();
        var isBundleDiscountApplied = pricingDto.appliedDiscounts().stream()
                .anyMatch(dc -> dc.getDiscountType() == DiscountType.BUNDLE);
        var isThereAnyOtherAppliedDiscount = pricingDto.appliedDiscounts().stream()
                .anyMatch(dc -> dc.getDiscountType() != DiscountType.NONE);


        // if(item == null) throw new IllegalArgumentException("Item cannot be null");
        if(quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");

        if(billableQuantity == null || billableQuantity <= 0)
            throw new IllegalArgumentException("Billable quantity must be greater than zero");

        if(pendingDiscounts.isEmpty() && !isThereAnyOtherAppliedDiscount)
            pricingDto.pendingDiscounts().add(DiscountContext.of(DiscountType.NONE, 0.0));

        if(pendingDiscounts.isEmpty()) {
            throw new IllegalArgumentException("Price calculation logic is completed, there are no pending discounts");
        }

        if(!quantity.equals(billableQuantity) && !isBundleDiscountApplied){
            throw new IllegalArgumentException("If quantity is different than billable quantity, BUNDLE discount must be applied or pending");
        }

        var newPricingDto = pricingDto;

        for(DiscountContext discountContext : new LinkedHashSet<>(pendingDiscounts)){
            var strategy = Invoice.pricingStrategies.get(discountContext.getDiscountType());
            newPricingDto = strategy.calculatePrice(newPricingDto);
        }

        return new Invoice(
                IdGenerator.nextInvoiceId(),
                item,
                newPricingDto.billedQuantity(),
                newPricingDto.currentTotal(),
                newPricingDto.currentTaxAmount(),
                newPricingDto.appliedDiscounts()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Invoice)) return false;
        return this.id.equals(((Invoice) obj).getId()) && Objects.equals(this.item.getId(), ((Invoice) obj).getItem().getId());
    }
}
