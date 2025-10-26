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

public class DiscountPricingStrategyTests {

    @Test
    public void singleDiscountShouldBeAppliedCorrectly(){

        var item = new Item("Test Item", BigDecimal.valueOf(837), 13);
        var discountContext = DiscountContext.of(DiscountType.DISCOUNT, 15.0);

        var pricingDto = new PricingDto(
                item,
                new LinkedHashSet<>(),
                new LinkedHashSet<>(){{add(discountContext);}},
                13,
                item.getTotalPrice(),
                item.getTaxAmountPerUnit()
        );

        //Price per unit with taxes: 987.66 and 15% discount -> 839.511
        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(10913.643).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(1964.455).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts()).contains(discountContext)
                .hasSize(1);

        assertThat(invoice.getBilledQuantity()).isEqualTo(invoice.getQuantity());
    }

    @Test
    public void multipleDiscountsShouldBeAppliedCorrectly(){

        var item = new Item("Test Item", BigDecimal.valueOf(588), 7);
        var discountContext1 = DiscountContext.of(DiscountType.DISCOUNT, 15.0);
        var discountContext2 = DiscountContext.of(DiscountType.DISCOUNT, 20.0);

        var pricingDto = new PricingDto(
                item,
                new LinkedHashSet<>(),
                new LinkedHashSet<>(){{add(discountContext1); add(discountContext2);}},
                7,
                item.getTotalPrice(),
                item.getTaxAmountPerUnit()
        );

        // Price per unit with taxes: 693.84 and 15% discount -> 589.76 and then 20% discount -> 471.81
        // 693.84 * 7 = 4856.88 -> 15% = 4128.348 -> 20% = 3302.6784
        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(3302.68).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(594.48).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts()).contains(discountContext1, discountContext2)
                .hasSize(2);

        assertThat(invoice.getBilledQuantity()).isEqualTo(invoice.getQuantity());
    }

    @Test
    public void discountShouldNotBeAppliedIfThereIsNoneDiscountApplied(){
        var item = new Item("Test Item", BigDecimal.valueOf(588), 7);
        var discountContext1 = DiscountContext.of(DiscountType.DISCOUNT, 15.0);
        var discountContext2 = DiscountContext.of(DiscountType.NONE, 0.0);

        var pricingDto = new PricingDto(
                item,
                new LinkedHashSet<>(){{add(discountContext2);}},
                new LinkedHashSet<>(){{add(discountContext1);}},
                7,
                item.getTotalPrice(),
                item.getTaxAmountPerUnit()
        );

        assertThatThrownBy(() -> Invoice.of(pricingDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("There is a NONE discount applied, no other discount can be applied");
    }

    @Test
    public void discountShouldNotBeAppliedIfThereIsInvalidDiscountPercentage(){

        assertThatThrownBy(() -> DiscountContext.of(DiscountType.DISCOUNT, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Percentage must be > 0 for DISCOUNT");

        assertThatThrownBy(() -> DiscountContext.of(DiscountType.DISCOUNT, -5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Percentage must be > 0 for DISCOUNT");

        assertThatThrownBy(() -> DiscountContext.of(DiscountType.DISCOUNT, 150.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Percentage must be <= 100 for DISCOUNT");
    }
}
