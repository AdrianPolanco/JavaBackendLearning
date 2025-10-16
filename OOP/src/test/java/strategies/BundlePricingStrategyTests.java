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

public class BundlePricingStrategyTests {

    @Test
    public void for12ItemsOnly8ShouldBeBilled(){
        var item = new Item("Test Item", BigDecimal.valueOf(256.73), 12);
        var discountContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var discountToApply = new LinkedHashSet<DiscountContext>(){{add(discountContext);}};

        var pricingDto = getPricingDto(item, new LinkedHashSet<>(), discountToApply, item.getQuantity(), item.getTotalPrice());

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(2423.52)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(436.23)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .hasSize(1);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(12)
                .isGreaterThan(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(8);
    }

    @Test
    public void for2ItemsBothShouldBeBilled(){
        var item = new Item("Test Item", BigDecimal.valueOf(256.73), 2);
        var discountContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var discountToApply = new LinkedHashSet<DiscountContext>(){{add(discountContext);}};

        var pricingDto = getPricingDto(item, new LinkedHashSet<>(), discountToApply, item.getQuantity(), item.getTotalPrice());

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(605.88)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(109.06)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .hasSize(1);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(2)
                .isEqualTo(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(2);
    }

    @Test
    public void for8ItemsOnly6ShouldBeBilled(){
        var item = new Item("Test Item", BigDecimal.valueOf(322.5), 8);
        var discountContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var discountToApply = new LinkedHashSet<DiscountContext>(){{add(discountContext);}};

        var pricingDto = getPricingDto(item, new LinkedHashSet<>(), discountToApply, item.getQuantity(), item.getTotalPrice());

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(2283.3)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(410.99)
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .hasSize(1);

        assertThat(invoice.getItem().getQuantity())
                .isEqualTo(8)
                .isGreaterThan(invoice.getBilledQuantity());

        assertThat(invoice.getBilledQuantity())
                .isEqualTo(6);
    }

    @Test
    public void shouldNotApplyBundleIfThereIsNoneDiscountApplied(){
        var item = new Item("Test Item", BigDecimal.valueOf(322.5), 8);
        var discountContext1 = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var discountContext2 = DiscountContext.of(DiscountType.NONE, 0.0);
        var discountToApply = new LinkedHashSet<DiscountContext>(){{add(discountContext1);}};
        var appliedDiscounts = new LinkedHashSet<DiscountContext>(){{add(discountContext2);}};

        var pricingDto = getPricingDto(item, appliedDiscounts, discountToApply, item.getQuantity(), item.getTotalPrice());

        assertThatThrownBy(() -> Invoice.of(pricingDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There is a NONE discount applied, no other discount can be applied");
    }

    @Test
    public void shouldNotApplyBundleIfAlreadyApplied(){
        var item = new Item("Test Item", BigDecimal.valueOf(322.5), 8);
        var discountContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var discountToApply = new LinkedHashSet<DiscountContext>(){{add(discountContext);}};
        var appliedDiscounts = new LinkedHashSet<DiscountContext>(){{add(discountContext);}};

        var pricingDto = getPricingDto(item, appliedDiscounts, discountToApply, item.getQuantity(), item.getTotalPrice());

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(item.getTotalPrice());

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(item.getTaxAmountPerUnit()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts())
                .contains(discountContext)
                .hasSize(1);

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
