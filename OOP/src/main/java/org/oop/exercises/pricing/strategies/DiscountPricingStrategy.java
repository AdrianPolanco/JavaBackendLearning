package org.oop.exercises.pricing.strategies;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.enums.TaxRate;
import org.oop.exercises.pricing.interfaces.PricingStrategy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DiscountPricingStrategy implements PricingStrategy {
    @Override
    public PricingDto calculatePrice(PricingDto pricingDto) {
        var item = pricingDto.item();
        var appliedDiscounts = new HashSet<>(pricingDto.appliedDiscounts());
        var pendingDiscounts = pricingDto.pendingDiscounts().stream().filter(
                discountContext -> discountContext.getDiscountType() == DiscountType.DISCOUNT
        ).collect(Collectors.toSet());
        var currentSubtotal = pricingDto.currentSubtotal();
        var currentTaxAmount = pricingDto.currentTaxAmount();
        var quantity = pricingDto.quantity();
        var billedQuantity = pricingDto.billedQuantity();

        var newSubtotal = currentSubtotal;
        var newTaxAmount = currentTaxAmount;

        for(var pendingDiscount: pendingDiscounts){
            var discountPercentage = pendingDiscount.getDiscountPercentage() / 100;

            newSubtotal = newSubtotal.multiply(
                    BigDecimal.ONE.subtract(newSubtotal.multiply(BigDecimal.valueOf(discountPercentage))));

            newTaxAmount = newSubtotal.multiply(TaxRate.STANDARD.getStandardRate());

            appliedDiscounts.add(pendingDiscount);
            appliedDiscounts.remove(pendingDiscount);
        }

        return new PricingDto(
                item,
                appliedDiscounts,
                pendingDiscounts,
                quantity,
                billedQuantity,
                newSubtotal,
                newTaxAmount
        );
    }
}
