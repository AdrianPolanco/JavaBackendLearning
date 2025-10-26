package org.oop.exercises.pricing.utils;

import org.oop.exercises.pricing.enums.DiscountType;

import java.util.Objects;

public class DiscountContext {
    private final DiscountType discountType;
    private final Double discountPercentage;

    private DiscountContext(DiscountType discountType, Double discountPercentage) {
        this.discountType = discountType;
        this.discountPercentage = discountPercentage;
    }

    public static DiscountContext of(DiscountType discountType, Double discountPercentage) {
        if (discountType == null) {
            throw new IllegalArgumentException("Discount type cannot be null");
        }

        if (discountType == DiscountType.DISCOUNT) {
            if (discountPercentage == null || discountPercentage <= 0) {
                throw new IllegalArgumentException("Percentage must be > 0 for DISCOUNT");
            }

            if(discountPercentage > 100)
                throw new IllegalArgumentException("Percentage must be <= 100 for DISCOUNT");

            return new DiscountContext(discountType, discountPercentage);
        }

        if(discountPercentage != 0.0)
            throw new IllegalArgumentException("Percentage must be 0 for NONE or BUNDLE");

        // For NONE or BUNDLE, ignore percentage
        return new DiscountContext(discountType, 0.0);
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountContext)) return false;
        DiscountContext that = (DiscountContext) o;
        return discountType == that.discountType &&
                Objects.equals(discountPercentage, that.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountType, discountPercentage);
    }

    @Override
    public String toString() {
        return discountType + (discountPercentage > 0 ? " (" + discountPercentage + "%)" : "");
    }
}

