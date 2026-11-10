package com.transport.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates the discount amount for an order.
 *
 * <ul>
 *   <li>amount &lt;= 0: no discount</li>
 *   <li>Premium, amount &gt;= 100: 20%; Premium, amount &lt; 100: 10%</li>
 *   <li>Not premium, amount &gt;= 200: 5%; otherwise no discount</li>
 * </ul>
 */
public class DiscountCalculator {

    private static final BigDecimal PREMIUM_HIGH_THRESHOLD = new BigDecimal("100");
    private static final BigDecimal STANDARD_THRESHOLD = new BigDecimal("200");

    private static final BigDecimal PREMIUM_HIGH_RATE = new BigDecimal("0.20");
    private static final BigDecimal PREMIUM_LOW_RATE = new BigDecimal("0.10");
    private static final BigDecimal STANDARD_RATE = new BigDecimal("0.05");

    private static final int SCALE = 2;

    public BigDecimal calculateDiscount(BigDecimal amount, CustomerType customerType) {
        if (amount == null || customerType == null) {
            throw new IllegalArgumentException("amount and customer type are required");
        }
        if (amount.signum() <= 0) {
            return BigDecimal.ZERO.setScale(SCALE);
        }
        return amount.multiply(rateFor(amount, customerType)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal rateFor(BigDecimal amount, CustomerType customerType) {
        if (customerType == CustomerType.PREMIUM) {
            return atLeast(amount, PREMIUM_HIGH_THRESHOLD) ? PREMIUM_HIGH_RATE : PREMIUM_LOW_RATE;
        }
        return atLeast(amount, STANDARD_THRESHOLD) ? STANDARD_RATE : BigDecimal.ZERO;
    }

    private static boolean atLeast(BigDecimal amount, BigDecimal threshold) {
        return amount.compareTo(threshold) >= 0;
    }
}
