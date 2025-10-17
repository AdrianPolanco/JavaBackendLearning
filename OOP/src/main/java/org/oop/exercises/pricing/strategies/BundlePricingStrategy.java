package org.oop.exercises.pricing.strategies;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.enums.TaxRate;
import org.oop.exercises.pricing.interfaces.PricingStrategy;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashSet;

public final class BundlePricingStrategy implements PricingStrategy {
    @Override
    public PricingDto calculatePrice(PricingDto pricingDto) {
        var item = pricingDto.item();
        var appliedDiscounts = new LinkedHashSet<>(pricingDto.appliedDiscounts());

        var wasBundleApplied = appliedDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.BUNDLE
        );

        if(wasBundleApplied){
            return pricingDto; // Ya se aplico el descuento de bundle, no hacer nada
        }

        var pendingDiscounts = new LinkedHashSet<>(pricingDto.pendingDiscounts());

        var isBundlePending = pendingDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.BUNDLE
        );

        if(!isBundlePending){
            return pricingDto; // No hay descuento de bundle pendiente, no hacer nada
        }

        var isThereNoneAppliedDiscount = appliedDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.NONE
        );

        if(isThereNoneAppliedDiscount) {
            throw new IllegalArgumentException("There is a NONE discount applied, no other discount can be applied");
        }

        var currentTotal = pricingDto.currentTotal();
        var currentTaxAmount = pricingDto.currentTaxAmount();
        var quantity = pricingDto.item().getQuantity();

        var newTotal = currentTotal;
        var newTaxAmount = currentTaxAmount;

        BigDecimal unitPrice = currentTotal.divide(BigDecimal.valueOf(quantity), MathContext.DECIMAL128);
        int chargeableQuantity = quantity - (quantity / 3); // Cada 3, 1 es gratis

        newTotal = unitPrice.multiply(BigDecimal.valueOf(chargeableQuantity));
        newTotal = newTotal.setScale(2, RoundingMode.HALF_UP);
        newTaxAmount = newTotal.multiply(TaxRate.STANDARD.getStandardRate())
                                    .setScale(2, RoundingMode.HALF_UP);

        appliedDiscounts.add(DiscountContext.of(DiscountType.BUNDLE, 0.0));
        pendingDiscounts.remove(DiscountContext.of(DiscountType.BUNDLE, 0.0));

        return new PricingDto(
                item,
                appliedDiscounts,
                pendingDiscounts,
                chargeableQuantity,
                newTotal,
                newTaxAmount
        );
    }
}
