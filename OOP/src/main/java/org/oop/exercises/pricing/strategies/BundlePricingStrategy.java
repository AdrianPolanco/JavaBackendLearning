package org.oop.exercises.pricing.strategies;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.enums.TaxRate;
import org.oop.exercises.pricing.interfaces.PricingStrategy;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;

public class BundlePricingStrategy implements PricingStrategy {
    @Override
    public PricingDto calculatePrice(PricingDto pricingDto) {
        var item = pricingDto.item();
        var appliedDiscounts = pricingDto.appliedDiscounts();

        var wasBundleApplied = appliedDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.BUNDLE
        );

        if(wasBundleApplied){
            return pricingDto; // Ya se aplico el descuento de bundle, no hacer nada
        }

        var pendingDiscounts = pricingDto.pendingDiscounts();

        var isBundlePending = pendingDiscounts.stream().anyMatch(
                discountContext -> discountContext.getDiscountType() == DiscountType.BUNDLE
        );

        if(!isBundlePending){
            return pricingDto; // No hay descuento de bundle pendiente, no hacer nada
        }

        var currentSubtotal = pricingDto.currentSubtotal();
        var currentTaxAmount = pricingDto.currentTaxAmount();
        var quantity = pricingDto.quantity();

        var newSubtotal = currentSubtotal;
        var newTaxAmount = currentTaxAmount;

        BigDecimal unitPrice = item.getUnitPriceWithTax();
        int chargeableQuantity = quantity - (quantity / 3); // Cada 3, 1 es gratis

        newSubtotal = unitPrice.multiply(BigDecimal.valueOf(chargeableQuantity));
        newTaxAmount = newSubtotal.multiply(TaxRate.STANDARD.getStandardRate());

        appliedDiscounts.add(DiscountContext.of(DiscountType.BUNDLE, 0.0));
        pendingDiscounts.remove(DiscountContext.of(DiscountType.BUNDLE, 0.0));

        return new PricingDto(
                item,
                appliedDiscounts,
                pendingDiscounts,
                quantity,
                chargeableQuantity,
                newSubtotal,
                newTaxAmount
        );
    }
}
