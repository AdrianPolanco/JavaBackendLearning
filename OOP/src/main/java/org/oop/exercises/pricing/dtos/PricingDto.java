package org.oop.exercises.pricing.dtos;

import org.oop.exercises.pricing.entities.Item;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.util.Set;

public record PricingDto(Item item,
                         Set<DiscountContext> appliedDiscounts,
                         Set<DiscountContext> pendingDiscounts,
                         Integer billedQuantity,
                         BigDecimal currentTotal,
                         BigDecimal currentTaxAmount) {

}
