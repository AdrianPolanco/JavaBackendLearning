package entities;

import org.junit.jupiter.api.Test;
import org.oop.exercises.pricing.dtos.PricingDto;
import org.oop.exercises.pricing.entities.Item;
import org.oop.exercises.pricing.entities.Order;
import org.oop.exercises.pricing.enums.DiscountType;
import org.oop.exercises.pricing.utils.DiscountContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OrderTests {

    @Test
    public void orderShouldBeCreated(){
        List<PricingDto> pricingDtos = getPricingDtos();

        var order = Order.of(pricingDtos);

        assertThat(order.getInvoicesList()).hasSize(4);

        assertThat(order.getTotalPrice())
                .isEqualTo(BigDecimal.valueOf(4799.26).setScale(2, RoundingMode.HALF_UP));
    }

    private static List<PricingDto> getPricingDtos(){
        var item1 = new Item("Item 1", BigDecimal.valueOf(125.32), 2);
        var item2 = new Item("Item 2", BigDecimal.valueOf(48.44), 4);
        var item3 = new Item("Item 3", BigDecimal.valueOf(256.73), 13);
        var item4 = new Item("Item 4", BigDecimal.valueOf(158.65), 14);

        return List.of(
                getPricingDto(
                        item1,
                        new LinkedHashSet<>(),
                        new LinkedHashSet<>(){{
                            add(DiscountContext.of(DiscountType.DISCOUNT, 10.0));
                            add(DiscountContext.of(DiscountType.DISCOUNT, 5.0));
                        }},
                        2,
                        item1.getTotalPrice()
                ),
                getPricingDto(
                        item2,
                        new LinkedHashSet<>(),
                        new LinkedHashSet<>(),
                        4,
                        item2.getTotalPrice()
                ),
                getPricingDto(
                        item3,
                        new LinkedHashSet<>(),
                        new LinkedHashSet<>(){{add(DiscountContext.of(DiscountType.BUNDLE, 0.0));}},
                        13,
                        item3.getTotalPrice()
                ),
                getPricingDto(
                        item4,
                        new LinkedHashSet<>(),
                        new LinkedHashSet<>(){{
                            add(DiscountContext.of(DiscountType.DISCOUNT, 15.0));
                            add(DiscountContext.of(DiscountType.BUNDLE, 0.0));
                        }},
                        14,
                        item4.getTotalPrice()
                )
        );
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
