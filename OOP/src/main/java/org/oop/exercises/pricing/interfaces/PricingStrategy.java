package org.oop.exercises.pricing.interfaces;

import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.util.Set;

public interface PricingStrategy {
    default PricingDto calculatePrice(PricingDto pricingDto) {
        var item = pricingDto.item();

        var baseDiscountContext = DiscountContext.of(DiscountType.NONE, 0.0);

        var hasOtherAppliedDiscounts = pricingDto.appliedDiscounts().stream()
                .anyMatch(discountContext -> discountContext.getDiscountType() != DiscountType.NONE);

        if(hasOtherAppliedDiscounts)
            throw new IllegalArgumentException("Cannot apply NONE discount when other discounts are applied");

        var hasOtherPendingDiscounts = pricingDto.pendingDiscounts().stream()
                .anyMatch(discountContext -> discountContext.getDiscountType() != DiscountType.NONE);

        if(hasOtherPendingDiscounts)
            throw new IllegalArgumentException("NONE discount calculation logic does not apply since there are other pending discounts");

        if(!pricingDto.billedQuantity().equals(pricingDto.item().getQuantity()))
            throw new IllegalArgumentException("Billed quantity must be equal to quantity when applying NONE discount");

        var pricePerUnit = item.getUnitPriceWithTax();

        var subTotal = pricePerUnit.multiply(BigDecimal.valueOf(pricingDto.item().getQuantity()));

        var totalTax = item.getTaxAmountPerUnit().multiply(BigDecimal.valueOf(pricingDto.billedQuantity()));

        return new PricingDto(
                item,
                Set.of(baseDiscountContext),
                pricingDto.pendingDiscounts(),
                pricingDto.billedQuantity(),
                subTotal,
                totalTax
        );
    }
}
