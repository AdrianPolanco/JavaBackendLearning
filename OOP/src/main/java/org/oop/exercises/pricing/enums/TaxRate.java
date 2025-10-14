package org.oop.exercises.pricing.enums;

import java.math.BigDecimal;

public enum TaxRate {
    STANDARD(BigDecimal.valueOf(0.18));
    private final BigDecimal standardRate;

    TaxRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }
}
