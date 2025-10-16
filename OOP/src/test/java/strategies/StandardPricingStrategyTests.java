package strategies;

import org.junit.jupiter.api.Test;
import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.entities.Invoice;
import org.oop.exercises.pricing.entities.Item;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.*;

public class StandardPricingStrategyTests {

    @Test
    public void invoiceWithNoDiscountsShouldBe708(){

        var pricingDto = getPricingDto();

        var invoice = Invoice.of(pricingDto);

        assertThat(invoice.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(708).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoice.getAppliedDiscounts()).contains(DiscountContext.of(DiscountType.NONE, 0.0))
                .hasSize(1);

        assertThat(invoice.getBilledQuantity()).isEqualTo(invoice.getQuantity());

        assertThat(invoice.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(108).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void invoiceShouldNotAcceptInconsistentQuantities(){
        var pricingDto = getPricingDto();

        var pricingDtoWithDifferentBilledQuantity = new PricingDto(
                pricingDto.item(),
                pricingDto.appliedDiscounts(),
                pricingDto.pendingDiscounts(),
                pricingDto.billedQuantity() - 1,
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithDifferentQuantityThatItem = new PricingDto(
                pricingDto.item(),
                pricingDto.appliedDiscounts(),
                pricingDto.pendingDiscounts(),
                pricingDto.billedQuantity() - 1,
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithNullQuantity = new PricingDto(
                new Item("Book", BigDecimal.valueOf(200), null),
                pricingDto.appliedDiscounts(),
                pricingDto.pendingDiscounts(),
                null,
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithZeroQuantity = new PricingDto(
                new Item("Book", BigDecimal.valueOf(200), 0),
                pricingDto.appliedDiscounts(),
                pricingDto.pendingDiscounts(),
                0,
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithNegativeQuantity = new PricingDto(
                new Item("Book", BigDecimal.valueOf(200), -1),
                pricingDto.appliedDiscounts(),
                pricingDto.pendingDiscounts(),
                -1,
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithDifferentBilledQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("If quantity is different than billable quantity, BUNDLE discount must be applied or pending");

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithDifferentQuantityThatItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("If quantity is different than billable quantity, BUNDLE discount must be applied or pending");

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithNullQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero");

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithZeroQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero");

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithNegativeQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    public void invoiceShouldNotAcceptOtherDiscounts(){
        var pricingDto = getPricingDto();
        var bundleContext = DiscountContext.of(DiscountType.BUNDLE, 0.0);
        var noneContext = DiscountContext.of(DiscountType.NONE, 0.0);

        var pricingDtoWithOtherAppliedDiscounts = new PricingDto(
                pricingDto.item(),
                new LinkedHashSet<>(){{add(bundleContext);}},
                new LinkedHashSet<>(){{add(noneContext);}},
                pricingDto.billedQuantity(),
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithOtherPendingDiscounts = new PricingDto(
                pricingDto.item(),
                new LinkedHashSet<>(),
                new LinkedHashSet<>(){{ add(noneContext); add(bundleContext); }},
                pricingDto.billedQuantity(),
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );

        var pricingDtoWithNoOtherAppliedDiscount = new PricingDto(
                pricingDto.item(),
                new LinkedHashSet<>(){{
                    add(DiscountContext.of(DiscountType.BUNDLE, 0.0));
                }},
                pricingDto.pendingDiscounts(),
                pricingDto.billedQuantity(),
                pricingDto.item().getTotalPrice(),
                pricingDto.item().getTaxAmountPerUnit()
        );


        assertThatThrownBy(() -> Invoice.of(pricingDtoWithOtherAppliedDiscounts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot apply NONE discount when other discounts are applied");


        assertThatThrownBy(() -> Invoice.of(pricingDtoWithOtherPendingDiscounts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("NONE discount calculation logic does not apply since there are other pending discounts");

        assertThatThrownBy(() -> Invoice.of(pricingDtoWithNoOtherAppliedDiscount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price calculation logic is completed, there are no pending discounts");
    }

    @Test
    public void shouldWorkWithEmptyDiscounts(){
        var pricingDto = getPricingDto();

        var invoiceWithPricingDto = Invoice.of(pricingDto);

        assertThat(invoiceWithPricingDto.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(708).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoiceWithPricingDto.getTotalTaxAmount())
                .isEqualTo(BigDecimal.valueOf(108).setScale(2, RoundingMode.HALF_UP));

        assertThat(invoiceWithPricingDto.getAppliedDiscounts())
                .contains(DiscountContext.of(DiscountType.NONE, 0.0))
                .hasSize(1);
    }

    private static PricingDto getPricingDto() {
        var item = new Item("Book", BigDecimal.valueOf(200), 3);

        return new PricingDto(
                item,
                new HashSet<>(),
                new HashSet<>(),
                item.getQuantity(),
                item.getTotalPrice(),
                item.getTaxAmountPerUnit().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
