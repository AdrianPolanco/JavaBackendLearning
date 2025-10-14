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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Invoice {
    private Integer id;
    private Item item;
    private Integer billableQuantity;
    private BigDecimal totalPrice;
    private BigDecimal taxAmount;
    private LocalDateTime createdAt;
    private Set<DiscountContext> appliedDiscounts;
    private static Map<DiscountType, PricingStrategy> pricingStrategies = Map.of(
            DiscountType.NONE, new StandardPricingStrategy(),
            DiscountType.DISCOUNT, new DiscountPricingStrategy(),
            DiscountType.BUNDLE, new BundlePricingStrategy()
    );

    private Invoice(Integer id, Item item, Integer billableQuantity, BigDecimal totalPrice, BigDecimal taxAmount, Set<DiscountContext> appliedDiscounts) {
        this.id = id;
        this.item = item;
        // EnumSet.of() usa menos memoria, es mas rapido y esta mas optimizado para enums que HashSet
        // this.discountTypes = EnumSet.of(DiscountType.NONE);
        this.billableQuantity = billableQuantity;
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

    public Set<DiscountType> getDiscountTypes() {
        return appliedDiscounts.stream().map(DiscountContext::getDiscountType).collect(Collectors.toSet());
    }

    /*private void setDiscountTypes(Set<DiscountContext> discountContexts) {
        var currentDiscountIsNone = this.getDiscountTypes().contains(DiscountType.NONE) && this.getDiscountTypes().size() == 1;

        var discountTypes = this.getDiscountTypes();

        var incomingDiscountIsNone = !discountTypes.isEmpty()
                && discountTypes.contains(DiscountType.NONE) && discountTypes.size() == 1;

        if(currentDiscountIsNone && incomingDiscountIsNone){
            System.out.println("No changes in discount types, both current and incoming are NONE");
            return;
        }

        if(discountTypes.contains(DiscountType.NONE) && discountTypes.size() > 1){
            System.out.println("Removing NONE from discount types as other discounts are present");
            discountTypes.remove(DiscountType.NONE);
        }

        if(incomingDiscountIsNone){
            this.discountContexts = Set.of(DiscountContext.of(DiscountType.NONE, 0.0));
        }

        boolean hasChanged = !this.getDiscountTypes().equals(discountTypes);

        this.discountContexts = discountContexts;

        if(hasChanged) recalculatePricing();
    }

    public void setDiscountPercentage(Double discountPercentage){
        if(discountPercentage == null || discountPercentage < 0 || discountPercentage > 100){
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        if(!this.getDiscountTypes().contains(DiscountType.DISCOUNT)){
            throw new IllegalArgumentException("Cannot set discount percentage when DISCOUNT type is not applied");
        }

        discountContexts = discountContexts.stream()
                .map(dc -> {
                    if(dc.getDiscountType() == DiscountType.DISCOUNT){
                        return DiscountContext.of(DiscountType.DISCOUNT, discountPercentage);
                    }
                    return dc;
                })
                .collect(Collectors.toSet());
    }*/

    public Set<DiscountContext> getDiscountPercentage(){
        return appliedDiscounts.stream().filter(
                discountContext -> discountContext.getDiscountType() == DiscountType.DISCOUNT
        ).collect(Collectors.toSet());
    }

    public Integer getBillableQuantity() {
        return billableQuantity;
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

    /*private void recalculatePricing(){
        var appliableStrategies = discountContexts.stream()
                .map(dc -> pricingStrategies.get(dc.getDiscountType()))
                .collect(Collectors.toSet());

        totalPrice = appliableStrategies.stream()
                .map(pricingStrategy -> pricingStrategy.calculatePrice());
    }*/

    public static Invoice of(PricingDto pricingDto){
        var item = pricingDto.item();
        var quantity = pricingDto.quantity();
        var billableQuantity = item.getQuantity();
        var pendingDiscounts = pricingDto.pendingDiscounts();
        var isBundleDiscountApplied = pendingDiscounts.stream()
                .anyMatch(dc -> dc.getDiscountType() == DiscountType.BUNDLE);

        // if(item == null) throw new IllegalArgumentException("Item cannot be null");
        if(quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");

        if(pendingDiscounts.isEmpty())
            pendingDiscounts.add(DiscountContext.of(DiscountType.NONE, 0.0));

        if(!quantity.equals(billableQuantity) && !isBundleDiscountApplied){
            throw new IllegalArgumentException("If quantity is different than billable quantity, BUNDLE discount must be applied");
        }

        var newPricingDto = pricingDto;

        for(DiscountContext discountContext : pendingDiscounts){
            var strategy = Invoice.pricingStrategies.get(discountContext.getDiscountType());

            newPricingDto = strategy.calculatePrice(newPricingDto);
        }

        return new Invoice(
                IdGenerator.nextInvoiceId(),
                item,
                newPricingDto.billedQuantity(),
                newPricingDto.currentSubtotal(),
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
