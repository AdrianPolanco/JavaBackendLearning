package strategies;

import org.junit.jupiter.api.Test;
import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.entities.Invoice;
import org.oop.exercises.pricing.entities.Item;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.*;

public class DiscountAndBundleStrategiesTest {

    @Test
    public void combinedDiscountAndBundleShouldBeAppliedCorrectly(){
        var item = new Item("Test Item", BigDecimal.valueOf(158.65), 14);
        var discountContext = DiscountContext.of(DiscountType.DISCOUNT, 10.0);
        var discountContext2 = DiscountContext.of(DiscountType.DISCOUNT, 15.0);
        var bundleContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);

        var pricingDto = getPricingDto(
                item,
                new LinkedHashSet<>(),
                new LinkedHashSet<>(){{add(discountContext); add(discountContext2); add(bundleContext);}},
                item.getQuantity(),
                item.getTotalPrice()
        );

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(1432.16)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(257.79)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .contains(discountContext2)
                .contains(bundleContext)
                .hasSize(3);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(14)
                .isGreaterThan(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(10);
    }

    @Test
    public void combinedBundleAndDiscountShouldBeAppliedCorrectly(){
        var item = new Item("Test Item", BigDecimal.valueOf(365.76), 8);

        var discountContext = DiscountContext.of(DiscountType.DISCOUNT, 20.0);
        var discountContext2 = DiscountContext.of(DiscountType.DISCOUNT, 5.0);
        var bundleContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);

        var pricingDto = getPricingDto(
                item,
                new LinkedHashSet<>(),
                new LinkedHashSet<>(){{add(bundleContext); add(discountContext); add(discountContext2);}},
                item.getQuantity(),
                item.getTotalPrice()
        );

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(1968.10)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(354.26)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .contains(discountContext2)
                .contains(bundleContext)
                .hasSize(3);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(8)
                .isGreaterThan(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(6);
    }

    @Test
    public void bundleShouldBeAppliedOnlyOnce(){
        var item = new Item("Test Item", BigDecimal.valueOf(365.76), 8);

        var discountContext = DiscountContext.of(DiscountType.DISCOUNT, 20.0);
        var discountContext2 = DiscountContext.of(DiscountType.DISCOUNT, 5.0);
        var bundleContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);

        var pricingDto = getPricingDto(
                item,
                new LinkedHashSet<>(){{add(bundleContext);}},
                new LinkedHashSet<>(){{add(bundleContext); add(discountContext); add(discountContext2);}},
                item.getQuantity(),
                item.getTotalPrice()
        );

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(2624.13)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(472.34)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .contains(discountContext2)
                .contains(bundleContext)
                .hasSize(3);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(8)
                .isEqualTo(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(8);
    }

    private static PricingDto getPricingDto(
            Item item,
            LinkedHashSet<DiscountContext> appliedContexts,
            LinkedHashSet<DiscountContext> pendingContexts,
            Integer billedQuantity,
            BigDecimal currentTotal
    ){
        return new PricingDto(
                item,
                appliedContexts,
                pendingContexts,
                billedQuantity,
                currentTotal,
                item.getTaxAmountPerUnit().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
