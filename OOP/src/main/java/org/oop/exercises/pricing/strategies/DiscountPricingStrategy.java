package org.oop.exercises.pricing.strategies;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.enums.TaxRate;
import org.oop.exercises.pricing.interfaces.PricingStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public final class DiscountPricingStrategy implements PricingStrategy {
    @Override
    public PricingDto calculatePrice(PricingDto pricingDto) {
        var item = pricingDto.item();
        var appliedDiscounts = new LinkedHashSet<>(pricingDto.appliedDiscounts());
        var discountContexts = pricingDto.pendingDiscounts().stream().filter(
                discountContext -> discountContext.getDiscountType() == DiscountType.DISCOUNT
        ).collect(Collectors.toCollection(LinkedHashSet::new));

        var pendingDiscounts = new LinkedHashSet<>(pricingDto.pendingDiscounts());

        var somePendingDiscountIsInvalid = discountContexts.stream().anyMatch(
                discountContext -> discountContext.getDiscountPercentage() < 0 || discountContext.getDiscountPercentage() > 100
        );

        var isThereNoneAppliedDiscount = appliedDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.NONE
        );

        if(somePendingDiscountIsInvalid){
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        if(isThereNoneAppliedDiscount) {
            throw new IllegalArgumentException("There is a NONE discount applied, no other discount can be applied");
        }

        var currentSubtotal = pricingDto.currentTotal();
        var currentTaxAmount = pricingDto.currentTaxAmount();
        var billedQuantity = pricingDto.billedQuantity();

        var newTotal = currentSubtotal;
        var newTaxAmount = currentTaxAmount;

        for(var pendingDiscount: discountContexts){

            var discountPercentage = BigDecimal.valueOf(pendingDiscount.getDiscountPercentage())
                                        .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

            newTotal = BigDecimal.ONE
                        .subtract(discountPercentage)
                        .multiply(newTotal);

            newTaxAmount = newTotal.multiply(TaxRate.STANDARD.getStandardRate());
        }

        newTotal = newTotal.setScale(2, RoundingMode.HALF_UP);
        newTaxAmount = newTaxAmount.setScale(2, RoundingMode.HALF_UP);

        appliedDiscounts.addAll(discountContexts);
        pendingDiscounts.removeAll(discountContexts);

        return new PricingDto(
                item,
                appliedDiscounts,
                pendingDiscounts,
                billedQuantity,
                newTotal,
                newTaxAmount
        );
    }
}
